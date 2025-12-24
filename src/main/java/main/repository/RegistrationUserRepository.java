package main.repository;

import main.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationUserRepository extends JpaRepository<UsersEntity, Long> {
}
