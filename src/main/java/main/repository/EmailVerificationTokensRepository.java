package main.repository;

import main.entity.EmailVerificationTokensEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokensRepository extends JpaRepository<EmailVerificationTokensEntity, Long> {
    Optional<EmailVerificationTokensEntity> searchByToken(String token);
}
