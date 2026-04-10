package main.repository;

import main.entity.MayorRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MayorRequestRepository extends JpaRepository<MayorRequestEntity, Long> {
    Optional<MayorRequestEntity> findByToken(String token);

    @Modifying
    @Query(value ="UPDATE mayor_request " +
            "SET status_id = (SELECT id FROM mayor_request_status WHERE status_name = 'EXPIRED'), " +
            "reviewed_at = now() " +
            "WHERE status_id = (SELECT id FROM mayor_request_status WHERE status_name = 'PENDING') " +
            "AND expired_at < now();"
            ,nativeQuery = true)
    int expiredOldRequests();
}
