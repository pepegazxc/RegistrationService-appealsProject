package main.repository;

import main.entity.MayorRequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MayorRequestStatusRepository extends JpaRepository<MayorRequestStatusEntity, Long> {
    Optional<MayorRequestStatusEntity> findByStatus(String status);
}
