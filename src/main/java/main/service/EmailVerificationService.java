package main.service;

import main.entity.EmailVerificationTokensEntity;
import main.entity.UsersEntity;
import main.repository.EmailVerificationTokensRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {
    private final EmailVerificationTokensRepository emailVerificationRepository;

    public EmailVerificationService(EmailVerificationTokensRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public String generateTokenForEmail(UsersEntity user){
        EmailVerificationTokensEntity email = buildEmailTokenEntity(user);

        emailVerificationRepository.save(email);

        return email.getToken();
    }

    private EmailVerificationTokensEntity buildEmailTokenEntity(UsersEntity user){
        return EmailVerificationTokensEntity.builder()
                .user(user)
                .token(generateToken())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .used(false)
                .build();
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }
}
