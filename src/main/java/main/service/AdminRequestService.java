package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.enums.AdminActionEnum;
import main.dto.request.AdminRequestActionRequest;
import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.repository.AdminRequestRepository;
import main.repository.AdminRequestStatusRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public AdminRequestService(AdminRequestRepository adminRequestRepository, UserRepository userRepository, RoleService roleService) {
        this.adminRequestRepository = adminRequestRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Transactional
    public void handleAdminRequest(String token, AdminRequestActionRequest actionRequest){
        String action = actionRequest.getAdminAction().toString();

        AdminRequestEntity request = findAdminRequest(token);
        checkOnUsed(request);
        checkToken(request);
        AdminRequestStatusEntity newStatus = findAdminRequestStatus(action);

        setNewStatusToAdminRequest(request, newStatus);

        UsersEntity user = request.getUser();
        RolesEntity newRole = actionRequest.getAdminAction() == AdminActionEnum.APPROVED
                ? roleService.findRole("admin")
                : roleService.findRole("user");

        setNewStatusToUser(user, newRole);

        adminRequestRepository.save(request);
        userRepository.save(user);
    }

    private void checkOnUsed(AdminRequestEntity request){
        if (request.getIsUsed()) throw new IllegalStateException();
    }

    private void checkToken(AdminRequestEntity request ){
        if (request.getExpiresAt().isBefore(LocalDateTime.now())){
            log.warn("Admin request token has expired {}", request.getUser().getUserIdentifier());
            throw new IllegalStateException();
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


}
