package main.repository;

import main.entity.MayorRequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MayorRequestStatusRepository extends JpaRepository<MayorRequestStatusEntity, Long> {
}
