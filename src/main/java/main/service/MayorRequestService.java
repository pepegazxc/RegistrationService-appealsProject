package main.service;

import main.dto.enums.RequestsActionEnum;
import main.dto.request.RequestsActionRequest;
import main.entity.MayorRequestEntity;
import main.entity.MayorRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.repository.MayorRequestRepository;
import main.repository.MayorRequestStatusRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MayorRequestService {

    private final MayorRequestStatusService requestStatusService;
    private final MayorRequestRepository mayorRequestRepository;
    private final CipherService cipher;
    private final ApplicationEventPublisher publisher;
    private final AdminsEmailsService adminsEmailsService;
    private final RoleService roleService;
    private final MayorRequestStatusRepository mayorRequestStatusRepository;

    public MayorRequestService(MayorRequestStatusService requestStatusService, MayorRequestRepository mayorRequestRepository, CipherService cipher, ApplicationEventPublisher publisher, AdminsEmailsService adminsEmailsService,RoleService roleService, MayorRequestStatusRepository mayorRequestStatusRepository) {
        this.requestStatusService = requestStatusService;
        this.mayorRequestRepository = mayorRequestRepository;
        this.cipher = cipher;
        this.publisher = publisher;
        this.adminsEmailsService = adminsEmailsService;
        this.roleService = roleService;
        this.mayorRequestStatusRepository = mayorRequestStatusRepository;
    }

    @Transactional
    public void addMayorRequest(UsersEntity user){
        MayorRequestStatusEntity status = requestStatusService.findMayorRequestStatus("PENDING");
        MayorRequestEntity mayorRequest = buildMayorRequest(user, status);

        mayorRequestRepository.save(mayorRequest);

        String mayorToken = mayorRequest.getToken();

        adminsEmailsService.sendMailToAdmins(mayorToken);
    }

    @Transactional
    public void handleMayorRequest(String token, RequestsActionRequest actionRequest){
        String action = actionRequest.getAction().toString();

        MayorRequestEntity mayorRequest = findMayorRequest(token);
        UsersEntity user = mayorRequest.getUser();
        MayorRequestStatusEntity mayorRequestStatus = findMayorRequestStatus(action);

        setNewStatusToMayorRequest(mayorRequest, mayorRequestStatus);

        RolesEntity role = actionRequest.getAction() == RequestsActionEnum.APPROVED
                ? roleService.findRole("mayor")
                : roleService.findRole("user");

        setNewRole(user, role);

    }

    private MayorRequestEntity buildMayorRequest(UsersEntity user, MayorRequestStatusEntity status){
        return MayorRequestEntity.builder()
                .user(user)
                .status(status)
                .token(generateTokenForMayorRequest())
                .createdAt(LocalDateTime.now())
                .reviewed_at(null)
                .isUsed(false)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();
    }

    private void setNewStatusToMayorRequest(MayorRequestEntity request, MayorRequestStatusEntity status){
        request.setStatus(status);
        request.setReviewed_at(LocalDateTime.now());
        request.setIsUsed(true);
    }

    private void setNewRole(UsersEntity user, RolesEntity role) { user.setRole(role); }

    private MayorRequestStatusEntity findMayorRequestStatus(String action){
        return mayorRequestStatusRepository.findByStatusName(action)
                .orElseThrow(() -> new IllegalStateException());
    }

    private MayorRequestEntity findMayorRequest(String token){
        return mayorRequestRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException());
    }

    private String generateTokenForMayorRequest(){
        return UUID.randomUUID().toString();
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }
}
