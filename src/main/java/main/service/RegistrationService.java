package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.enums.RolesEnum;
import main.dto.request.UserRequest;
import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.RegistrationEvent;
import main.exception.user.UserNotFoundException;
import main.repository.AdminRequestRepository;
import main.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final PasswordEncoder encoder;
    private final UserIdentifierService userIdentifier;
    private final EmailVerificationService emailService;
    private final ApplicationEventPublisher publisher;
    private final RoleService roleService;

    public RegistrationService(UserRepository userRepository, CipherService cipher, PasswordEncoder encoder, UserIdentifierService userIdentifier, EmailVerificationService emailService, ApplicationEventPublisher publisher, RoleService roleService) {
        this.userRepository = userRepository;
        this.cipher = cipher;
        this.encoder = encoder;
        this.userIdentifier = userIdentifier;
        this.emailService = emailService;
        this.publisher = publisher;
        this.roleService = roleService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String cipherEmail = cipher.encrypt(email);

        UsersEntity user = findByCipherEmail(cipherEmail);

        log.debug("User details loaded for identifier {}", user.getUserIdentifier());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserIdentifier())
                .password(user.getHashPassword())
                .roles(user.getRole().getRoleName())
                .build();
    }

    @Transactional
    public void registration(UserRequest request){
        String userRole = request.getRole().toString();

        RolesEntity role = switch (userRole){
            case "admin" -> roleService.findRole("PENDING_ADMIN");
            case "mayor" -> roleService.findRole("PENDING_MAYOR");
            default -> roleService.findRole(userRole);
        };

        UsersEntity user = addNewUser(request, role);

        userRepository.save(user);
        log.info("New user has been registered. Unique identifier {}", user.getUserIdentifier());


        String token = emailService.generateTokenForEmail(user);

        publisher.publishEvent(new RegistrationEvent(
                decryptEmail(user.getCipherEmail()),
                token
                )
        );
    }

    private String generateUserIdentifier(){
        String identifier;
        do{
            identifier = userIdentifier.generate();
        } while (userRepository.existsByUserIdentifier(identifier));
        return identifier;
    }

    
    private UsersEntity findByCipherEmail(String cipherEmail){
        return userRepository.findByCipherEmail(cipherEmail)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: User not found");
                    return new UserNotFoundException();
                });
    }

    private UsersEntity addNewUser(UserRequest request, RolesEntity role){
        return UsersEntity.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .userIdentifier(generateUserIdentifier())
                .cipherEmail(cipher.encrypt(request.getEmail()))
                .cipherPhoneNumber(cipher.encrypt(request.getPhoneNumber()))
                .hashPassword(encoder.encode(request.getPassword()))
                .role(role)
                .isEmailVerified(false)
                .isActive(true)
                .build();
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }

}
