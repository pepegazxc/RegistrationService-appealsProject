package main.service;

import lombok.extern.slf4j.Slf4j;
import main.entity.AdminRequestStatusEntity;
import main.repository.AdminRequestStatusRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminRequestStatusService {

    private final AdminRequestStatusRepository adminRequestStatusRepository;

    public AdminRequestStatusService(AdminRequestStatusRepository adminRequestStatusRepository) {
        this.adminRequestStatusRepository = adminRequestStatusRepository;
    }

    private AdminRequestStatusEntity findAdminRequestStatus(String status){
        return adminRequestStatusRepository.findByStatus(status)
                .orElseThrow(() -> {
                    log.warn("Can't find admin request status");
                    throw new IllegalStateException();
                });
    }
}
