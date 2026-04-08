package main.repository;

import main.entity.MayorRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MayorRequestRepository extends JpaRepository<MayorRequestEntity, Long> {
}
