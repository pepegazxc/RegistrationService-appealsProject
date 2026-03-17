package main.service;

import lombok.extern.slf4j.Slf4j;
import main.entity.EmailVerificationTokensEntity;
import main.entity.UsersEntity;
import main.exception.email.EmailTokenNotFoundException;
import main.exception.email.ExpiredEmailTokenException;
import main.exception.email.UsedEmailTokenException;
import main.exception.email.VerifiedUserEmailException;
import main.repository.EmailVerificationTokensRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class EmailVerificationService {
    private final EmailVerificationTokensRepository emailVerificationRepository;

    public EmailVerificationService(EmailVerificationTokensRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public String generateTokenForEmail(UsersEntity user){
        EmailVerificationTokensEntity email = buildEmailTokenEntity(user);

        emailVerificationRepository.save(email);
        log.info("User {} has got his email verification token", user.getUserIdentifier());

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
        log.info("User {} successfully verified mail", user.getUserIdentifier());
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
        if (email.getExpiresAt().isBefore(LocalDateTime.now())){
            log.warn("Email verification token for user {} is expired", email.getUser().getUserIdentifier());
            throw new ExpiredEmailTokenException();
        }
        if (email.getUsed()) {
            log.warn("Email verification token for user {} is used", email.getUser().getUserIdentifier());
            throw new UsedEmailTokenException();
        }
    }

    private void checkEmailOnConfirm(UsersEntity user){
        if (user.getIsEmailVerified()){
            log.warn("User {} has already verified his email", user.getUserIdentifier());
            throw new VerifiedUserEmailException();
        }
    }

    private void confirmEmail(UsersEntity user){
        user.setIsEmailVerified(true);
    }

    private void tokenIsUsed(EmailVerificationTokensEntity email){
        email.setUsed(true);
    }

    private EmailVerificationTokensEntity searchByToken(String token){
        EmailVerificationTokensEntity email = emailVerificationRepository.searchByToken(token)
                .orElseThrow(() ->{
                    log.warn("Verification token {} not found", token);
                    throw new EmailTokenNotFoundException();
                });

        return email;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }
}
