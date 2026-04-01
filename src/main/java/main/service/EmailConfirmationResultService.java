package main.service;

import main.dto.response.EmailConfirmResultResponse;
import main.entity.UsersEntity;
import main.service.jwt.AuthTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailConfirmationResultService {

    private final AuthTokenService jwt;
    private final EmailVerificationService email;
    private final AdminRequestService adminRequestService;

    public EmailConfirmationResultService(AuthTokenService jwt, EmailVerificationService email, AdminRequestService adminRequestService) {
        this.jwt = jwt;
        this.email = email;
        this.adminRequestService = adminRequestService;
    }

    @Transactional
    public EmailConfirmResultResponse confirmationResult(String token) {
        UsersEntity user = email.confirmUserEmail(token);

        if (user.getRole().getRoleName().equals("USER")) {
            String jwtToken = jwt.generateJwtTokenForCurrentUser();

            return EmailConfirmResultResponse.userEmailConfirmed(jwtToken);
        }

        adminRequestService.addAdminRequest(user);

        return EmailConfirmResultResponse.adminEmailConfirmed();
    }
}
