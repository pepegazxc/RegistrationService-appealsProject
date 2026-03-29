package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.enums.AdminActionEnum;
import main.dto.enums.RolesEnum;
import main.dto.request.AdminRequestActionRequest;
import main.dto.request.UserRequest;
import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.AdminRequestEvent;
import main.event.UserRegisteredEvent;
import main.exception.user.RoleNotFoundException;
import main.exception.user.UserNotFoundException;
import main.repository.AdminRequestRepository;
import main.repository.AdminRequestStatusRepository;
import main.repository.RolesRepository;
import main.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final PasswordEncoder encoder;
    private final UserIdentifierService userIdentifier;
    private final RolesRepository rolesRepository;
    private final EmailVerificationService emailService;
    private final ApplicationEventPublisher publisher;
    private final AdminRequestStatusRepository adminRequestStatusRepository;
    private final AdminRequestRepository adminRequestRepository;

    public UserService(UserRepository userRepository, CipherService cipher, PasswordEncoder encoder, UserIdentifierService userIdentifier, RolesRepository rolesRepository, EmailVerificationService emailService, ApplicationEventPublisher publisher, AdminRequestStatusRepository adminRequestStatusRepository, AdminRequestRepository adminRequestRepository) {
        this.userRepository = userRepository;
        this.cipher = cipher;
        this.encoder = encoder;
        this.userIdentifier = userIdentifier;
        this.rolesRepository = rolesRepository;
        this.emailService = emailService;
        this.publisher = publisher;
        this.adminRequestStatusRepository = adminRequestStatusRepository;
        this.adminRequestRepository = adminRequestRepository;
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

        RolesEntity role = request.getRole() == RolesEnum.admin
                            ? findRole("PENDING_ADMIN")
                            : findRole(userRole);

        UsersEntity user = addNewUser(request, role);


        userRepository.save(user);
        log.info("New user has been registered. Unique identifier {}", user.getUserIdentifier());



        if (request.getRole() == RolesEnum.admin){
            handleAdminRegistration(user);
        }else{
            String token = emailService.generateTokenForEmail(user);

            publisher.publishEvent(new UserRegisteredEvent(
                            decryptEmail(user.getCipherEmail()),
                            token
                    )
            );
        }

    }



    private void handleAdminRegistration(UsersEntity user){
        AdminRequestStatusEntity status = findAdminRequestStatus("PENDING");
        AdminRequestEntity admin = buildAdminRequest(user, status);

        adminRequestRepository.save(admin);

        String token = emailService.generateTokenForEmail(user);

        publisher.publishEvent(
                new AdminRequestEvent(
                        token,
                        decryptEmail(admin.getUser().getCipherEmail())
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

    private AdminRequestEntity buildAdminRequest(UsersEntity user, AdminRequestStatusEntity status){
        return AdminRequestEntity.builder()
                .user(user)
                .status(status)
                .token(generateTokenForAdmin())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .reviewedAt(null)
                .isUsed(false)
                .build();
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

    private AdminRequestStatusEntity findAdminRequestStatus(String status){
        return adminRequestStatusRepository.findByStatus(status)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request status");
                    throw new IllegalStateException();
                });
    }

    private RolesEntity findRole(String role){
        return rolesRepository.findByRoleName(role.trim().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException());
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }

    private String generateTokenForAdmin() {return UUID.randomUUID().toString();}

}
