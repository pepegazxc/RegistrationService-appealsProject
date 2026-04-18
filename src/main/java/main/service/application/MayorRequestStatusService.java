package main.service.application;

import lombok.extern.slf4j.Slf4j;
import main.entity.MayorRequestStatusEntity;
import main.exception.request.MayorRequestStatusNotFoundException;
import main.repository.MayorRequestStatusRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MayorRequestStatusService {

    private final MayorRequestStatusRepository statusRepository;

    public MayorRequestStatusService(MayorRequestStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public MayorRequestStatusEntity findMayorRequestStatus(String statusName){
        return statusRepository.findByStatusName(statusName)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request status {}", statusName);
                    throw new MayorRequestStatusNotFoundException();
                });
    }
}
