package main.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.dto.request.UserRequest;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.RegistrationEvent;
import main.exception.user.UserIdentifierException;
import main.exception.user.UserNotFoundException;
import main.producer.KafkaProducer;
import main.repository.UserRepository;
import main.service.infrastructure.CipherService;
import main.service.support.UserIdentifierService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final PasswordEncoder encoder;
    private final UserIdentifierService userIdentifier;
    private final EmailVerificationService emailService;
    private final KafkaProducer kafka;
    private final RoleService roleService;

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

        kafka.handleRegistration(
                decryptEmail(user.getCipherEmail()),
                token
        );
    }

    private String generateUserIdentifier(){
        int attempts = 0;
        String identifier;
        do{
            identifier = userIdentifier.generate();
            attempts++;
            if(attempts > 10) throw new UserIdentifierException();
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
