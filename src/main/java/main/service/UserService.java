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


        if (request.getRole() == RolesEnum.admin){
            handleAdminRegistration(user);
        }

        userRepository.save(user);
        log.info("New user has been registered. Unique identifier {}", user.getUserIdentifier());

        String token = emailService.generateTokenForEmail(user);

        publisher.publishEvent(new UserRegisteredEvent(
                decryptEmail(user.getCipherEmail()),
                token
                )
        );
    }

    @Transactional
    public void handleAdminRequest(String token, AdminRequestActionRequest actionRequest){
        String action = actionRequest.getAdminAction().toString();

        AdminRequestEntity request = findAdminRequest(token);
        AdminRequestStatusEntity newStatus = findAdminRequestStatus(action);

        setNewStatusToAdminRequest(request, newStatus);

        UsersEntity user = request.getUser();
        RolesEntity newRole = actionRequest.getAdminAction() == AdminActionEnum.APPROVED
                                ? findRole("admin")
                                : findRole("user");

        setNewStatusToUser(user, newRole);

        adminRequestRepository.save(request);
        userRepository.save(user);
    }

    private void handleAdminRegistration(UsersEntity user){
        AdminRequestStatusEntity status = findAdminRequestStatus("PENDING");
        AdminRequestEntity admin = buildAdminRequest(user, status);

        adminRequestRepository.save(admin);

        publisher.publishEvent(
                new AdminRequestEvent(
                        admin.getToken(),
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
                .token(generateToken())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .reviewedAt(null)
                .build();
    }

    private AdminRequestEntity findAdminRequest(String token){
        return adminRequestRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request with token: {}", token);
                    throw new IllegalStateException();
                });
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
                .orElseThrow(() -> new IllegalStateException());
    }

    private void setNewStatusToUser(UsersEntity user,RolesEntity role){
        user.setRole(role);
    }

    private void setNewStatusToAdminRequest(AdminRequestEntity adminRequest, AdminRequestStatusEntity status){
        adminRequest.setStatus(status);
    }

    private RolesEntity findRole(String role){
        return rolesRepository.findByRoleName(role.trim().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException());
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }

    private String generateToken() {return UUID.randomUUID().toString();}

}
