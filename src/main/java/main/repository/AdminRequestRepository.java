package main.repository;

import main.entity.AdminRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRequestRepository extends JpaRepository<AdminRequestEntity, Long> {
    Optional<AdminRequestEntity> findByToken(String token);

    @Modifying
    @Query(value ="UPDATE admin_request " +
            "SET status_id = (SELECT id FROM admin_request_status WHERE status = 'EXPIRED'), " +
            "reviewed_at = now() " +
            "WHERE status_id = (SELECT id FROM admin_request_status WHERE status = 'PENDING') " +
            "AND expires_at < now();"
            ,nativeQuery = true)
    int expireOldRequests();
}
