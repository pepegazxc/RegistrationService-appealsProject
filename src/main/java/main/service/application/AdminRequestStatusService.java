package main.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.entity.AdminRequestStatusEntity;
import main.exception.request.AdminRequestStatusNotFoundException;
import main.repository.AdminRequestStatusRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminRequestStatusService {

    private final AdminRequestStatusRepository adminRequestStatusRepository;

    public AdminRequestStatusEntity findAdminRequestStatus(String status){
        return adminRequestStatusRepository.findByStatus(status)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request status {}", status);
                    throw new AdminRequestStatusNotFoundException();
                });
    }
}
