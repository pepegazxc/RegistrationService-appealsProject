package main.repository;

import main.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RegistrationUserRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByUserIdentifier(String userIdentifier);
    Optional<UsersEntity> findByCipherEmail(String email);
}
