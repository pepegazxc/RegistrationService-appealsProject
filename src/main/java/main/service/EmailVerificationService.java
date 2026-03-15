package main.service;

import main.entity.EmailVerificationTokensEntity;
import main.entity.UsersEntity;
import main.repository.EmailVerificationTokensRepository;
import main.repository.RolesRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {
    private final EmailVerificationTokensRepository emailVerificationRepository;
    private final UserRepository userRepository;

    public EmailVerificationService(EmailVerificationTokensRepository emailVerificationRepository, UserRepository userRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.userRepository = userRepository;
    }

    public String generateTokenForEmail(UsersEntity user){
        EmailVerificationTokensEntity email = buildEmailTokenEntity(user);

        emailVerificationRepository.save(email);

        return email.getToken();
    }

    @Transactional
    public void confirmUserEmail(String token){
        EmailVerificationTokensEntity email = searchByToken(token);
        checkEmailToken(email);

        UsersEntity user = email.getUser();
        checkEmailOnConfirm(user);

        confirmEmail(user);
        tokenIsUsed(email);
    }

    private EmailVerificationTokensEntity buildEmailTokenEntity(UsersEntity user){
        return EmailVerificationTokensEntity.builder()
                .user(user)
                .token(generateToken())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .used(false)
                .build();
    }

    private void checkEmailToken(EmailVerificationTokensEntity email){
        if (email.getExpiresAt().isBefore(LocalDateTime.now())) throw  new IllegalStateException();
        if (email.getUsed()) throw new IllegalStateException();
    }

    private void checkEmailOnConfirm(UsersEntity user){
        if (user.getIsEmailVerified()) throw new IllegalStateException();
    }

    private void confirmEmail(UsersEntity user){
        user.setIsEmailVerified(true);
    }

    private void tokenIsUsed(EmailVerificationTokensEntity email){
        email.setUsed(true);
    }

    private EmailVerificationTokensEntity searchByToken(String token){
        EmailVerificationTokensEntity email = emailVerificationRepository.searchByToken(token)
                .orElseThrow(() -> new IllegalStateException());

        return email;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }
}
