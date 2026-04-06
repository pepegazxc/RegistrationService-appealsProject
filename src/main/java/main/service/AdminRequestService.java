package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.enums.AdminActionEnum;
import main.dto.request.AdminRequestActionRequest;
import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.AdminRequestEvent;
import main.event.AdminRequestResponseEvent;
import main.event.RegistrationEvent;
import main.exception.request.AdminRequestIsExpiredException;
import main.repository.AdminRequestRepository;
import main.repository.AdminRequestStatusRepository;
import main.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final ApplicationEventPublisher publisher;
    private final RoleService roleService;
    private final AdminRequestStatusService adminRequestStatusService;
    private final CipherService cipher;

    public AdminRequestService(AdminRequestRepository adminRequestRepository, ApplicationEventPublisher publisher, RoleService roleService, AdminRequestStatusService adminRequestStatusService,CipherService cipher) {
        this.adminRequestRepository = adminRequestRepository;
        this.publisher = publisher;
        this.roleService = roleService;
        this.adminRequestStatusService = adminRequestStatusService;
        this.cipher = cipher;
    }

    @Transactional
    public void handleAdminRequest(String token, AdminRequestActionRequest actionRequest){
        String action = actionRequest.getAdminAction().toString();

        AdminRequestEntity request = findAdminRequest(token);
        checkOnUsed(request);
        checkToken(request);
        AdminRequestStatusEntity newStatus = adminRequestStatusService.findAdminRequestStatus(action);

        setNewStatusToAdminRequest(request, newStatus);

        UsersEntity user = request.getUser();
        RolesEntity newRole = actionRequest.getAdminAction() == AdminActionEnum.APPROVED
                ? roleService.findRole("admin")
                : roleService.findRole("user");

        setNewStatusToUser(user, newRole);

        publisher.publishEvent(
                new AdminRequestResponseEvent(
                        decryptEmail(user.getCipherEmail()),
                        actionRequest.getAdminAction()
                )
        );
    }

    @Transactional
    public void addAdminRequest(UsersEntity user){
        AdminRequestStatusEntity status = adminRequestStatusService.findAdminRequestStatus("PENDING");
        AdminRequestEntity admin = buildAdminRequest(user, status);

        adminRequestRepository.save(admin);

        String token = admin.getToken();

        publisher.publishEvent(
                new AdminRequestEvent(
                        token,
                        decryptEmail(admin.getUser().getCipherEmail())
                )
        );
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


    private void checkOnUsed(AdminRequestEntity request){
        if (request.getIsUsed()) throw new IllegalStateException();
    }

    private void checkToken(AdminRequestEntity request ){
        if (request.getExpiresAt().isBefore(LocalDateTime.now())){
            log.warn("Admin request token has expired {}", request.getUser().getUserIdentifier());
            throw new AdminRequestIsExpiredException();
        }

    }

    private AdminRequestEntity findAdminRequest(String token){
        return adminRequestRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request with token: {}", token);
                    throw new IllegalStateException();
                });
    }


    private void setNewStatusToUser(UsersEntity user,RolesEntity role){
        user.setRole(role);
    }

    private void setNewStatusToAdminRequest(AdminRequestEntity adminRequest, AdminRequestStatusEntity status){
        adminRequest.setStatus(status);
        adminRequest.setReviewedAt(LocalDateTime.now());
        adminRequest.setIsUsed(true);
    }


    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }

    private String generateTokenForAdmin() {return UUID.randomUUID().toString();}


}
