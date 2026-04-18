package main.service.application;

import main.dto.response.EmailConfirmResultResponse;
import main.entity.UsersEntity;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailConfirmationResultService {

    private final AuthTokenService jwt;
    private final EmailVerificationService email;
    private final AdminRequestService adminRequestService;
    private final MayorRequestService mayorRequestService;

    public EmailConfirmationResultService(AuthTokenService jwt, EmailVerificationService email, AdminRequestService adminRequestService, MayorRequestService mayorRequestService) {
        this.jwt = jwt;
        this.email = email;
        this.adminRequestService = adminRequestService;
        this.mayorRequestService = mayorRequestService;
    }

    @Transactional
    public EmailConfirmResultResponse confirmationResult(String token) {
        UsersEntity user = email.confirmUserEmail(token);


        switch (user.getRole().getRoleName()){
            case "USER" -> {
                String jwtToken = jwt.generateJwtTokenForCurrentUser();

                return EmailConfirmResultResponse.userEmailConfirmed(jwtToken);
            }

            case "PENDING_ADMIN" -> {
                adminRequestService.addAdminRequest(user);

                return EmailConfirmResultResponse.adminEmailConfirmed();
            }

            case "PENDING_MAYOR" -> {
                mayorRequestService.addMayorRequest(user);

                return EmailConfirmResultResponse.mayorEmailConfirmed();
            }

            default -> throw new  IllegalArgumentException();
        }
    }
}
