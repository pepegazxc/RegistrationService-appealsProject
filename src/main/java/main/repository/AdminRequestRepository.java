package main.repository;

import main.entity.AdminRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRequestRepository extends JpaRepository<AdminRequestEntity, Long> {
    Optional<AdminRequestEntity> findByToken(String token);
}
