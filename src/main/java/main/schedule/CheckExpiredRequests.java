package main.schedule;

import lombok.extern.slf4j.Slf4j;
import main.repository.AdminRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CheckExpiredRequests {

    private final AdminRequestRepository adminRequestRepository;

    public CheckExpiredRequests(AdminRequestRepository adminRequestRepository) {
        this.adminRequestRepository = adminRequestRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredAdminRequests(){
        int request = adminRequestRepository.expireOldRequests();
        log.info("Expired admin requests {}", request);
    }
}
