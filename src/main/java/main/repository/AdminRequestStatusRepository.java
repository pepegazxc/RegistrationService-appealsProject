package main.repository;

import main.entity.AdminRequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRequestStatusRepository extends JpaRepository<AdminRequestStatusEntity, Long> {
}
