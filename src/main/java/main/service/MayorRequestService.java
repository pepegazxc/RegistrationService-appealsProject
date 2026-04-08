package main.service;

import main.entity.MayorRequestEntity;
import main.entity.MayorRequestStatusEntity;
import main.entity.UsersEntity;
import main.repository.MayorRequestRepository;
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

    public MayorRequestService(MayorRequestStatusService requestStatusService, MayorRequestRepository mayorRequestRepository, CipherService cipher, ApplicationEventPublisher publisher) {
        this.requestStatusService = requestStatusService;
        this.mayorRequestRepository = mayorRequestRepository;
        this.cipher = cipher;
        this.publisher = publisher;
    }

    @Transactional
    public void addMayorRequest(UsersEntity user){
        MayorRequestStatusEntity status = requestStatusService.findMayorRequestStatus("PENDING");
        MayorRequestEntity mayorRequest = buildMayorRequest(user, status);

        mayorRequestRepository.save(mayorRequest);

        String mayorToken = mayorRequest.getToken();

        Object obj = null;
        publisher.publishEvent(obj);
    }

    private MayorRequestEntity buildMayorRequest(UsersEntity user, MayorRequestStatusEntity status){
        return MayorRequestEntity.builder()
                .user(user)
                .status(status)
                .token(generateTokenForMayorRequest())
                .createdAt(LocalDateTime.now())
                .reviewed_at(null)
                .isUsed(false)
                .build();
    }

    private String generateTokenForMayorRequest(){
        return UUID.randomUUID().toString();
    }

    private String decryptEmail(String email){
        return cipher.decrypt(email);
    }
}
