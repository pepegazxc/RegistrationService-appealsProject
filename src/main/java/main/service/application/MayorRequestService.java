package main.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.dto.enums.RequestsActionEnum;
import main.dto.request.RequestsActionRequest;
import main.entity.MayorRequestEntity;
import main.entity.MayorRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.exception.request.MayorRequestNotFoundException;
import main.exception.request.MayorRequestStatusNotFoundException;
import main.exception.request.RequestExpiredException;
import main.exception.request.RequestIsUsedException;
import main.producer.KafkaProducer;
import main.repository.MayorRequestRepository;
import main.repository.MayorRequestStatusRepository;
import main.service.infrastructure.CipherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MayorRequestService {

    private final MayorRequestStatusService requestStatusService;
    private final MayorRequestRepository mayorRequestRepository;
    private final AdminsEmailsService adminsEmailsService;
    private final RoleService roleService;
    private final MayorRequestStatusRepository mayorRequestStatusRepository;
    private final KafkaProducer kafka;
    private final CipherService cipher;

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
        checkMayorRequestToken(mayorRequest);
        UsersEntity user = mayorRequest.getUser();
        MayorRequestStatusEntity mayorRequestStatus = findMayorRequestStatus(action);

        setNewStatusToMayorRequest(mayorRequest, mayorRequestStatus);

        RolesEntity role = actionRequest.getAction() == RequestsActionEnum.APPROVED
                ? roleService.findRole("mayor")
                : roleService.findRole("user");

        setNewRole(user, role);

        kafka.handleMayorRequestMail(
                decryptEmail(user.getCipherEmail()),
                token
        );
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

    private void checkMayorRequestToken(MayorRequestEntity request){
        if (request.getExpiredAt().isBefore(LocalDateTime.now())){
            log.warn("Mayor request has expired, {}", request.getUser().getUserIdentifier());
            throw new RequestExpiredException();
        }
        if (request.getIsUsed()) throw new RequestIsUsedException();
    }

    private void setNewStatusToMayorRequest(MayorRequestEntity request, MayorRequestStatusEntity status){
        request.setStatus(status);
        request.setReviewed_at(LocalDateTime.now());
        request.setIsUsed(true);
    }

    private void setNewRole(UsersEntity user, RolesEntity role) { user.setRole(role); }

    private MayorRequestStatusEntity findMayorRequestStatus(String action){
        return mayorRequestStatusRepository.findByStatus(action)
                .orElseThrow(() -> new MayorRequestStatusNotFoundException());
    }

    private MayorRequestEntity findMayorRequest(String token){
        return mayorRequestRepository.findByToken(token)
                .orElseThrow(() -> new MayorRequestNotFoundException());
    }

    private String generateTokenForMayorRequest(){
        return UUID.randomUUID().toString();
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }
}
