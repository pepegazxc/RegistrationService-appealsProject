package main.repository;

import main.entity.AdminRequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRequestStatusRepository extends JpaRepository<AdminRequestStatusEntity, Long> {
    Optional<AdminRequestStatusEntity> findByStatus(String status);
}
