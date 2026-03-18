package main.repository;

import main.entity.AdminRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRequestRepository extends JpaRepository<AdminRequestEntity, Long> {
}
