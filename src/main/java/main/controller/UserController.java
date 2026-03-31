package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import main.dto.request.AdminRequestActionRequest;
import main.dto.response.AuthResponse;
import main.dto.response.ConfirmAdminRequestResponse;
import main.dto.response.EmailConfirmationResponse;
import main.dto.response.RefreshTokenResponse;
import main.dto.request.UserRequest;
import main.entity.UsersEntity;
import main.service.*;
import main.service.jwt.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    private final RegistrationService registrationService;
    private final AdminRequestService adminRequestService;
    private final AuthService auth;
    private final AuthTokenService jwt;
    private final EmailVerificationService email;

    public UserController(RegistrationService registrationService, AdminRequestService adminRequestService, AuthService auth, AuthTokenService jwt, EmailVerificationService email) {
        this.registrationService = registrationService;
        this.adminRequestService = adminRequestService;
        this.auth = auth;
        this.jwt = jwt;
        this.email = email;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest){
        log.info("Registration attempt email={}", request.getEmail());
        registrationService.registration(request);
        auth.autoAuth(request.getEmail(), request.getPassword(), httpRequest);

        log.info("Registration success email={}", request.getEmail());
        return ResponseEntity.ok(
                        new AuthResponse(
                                "User has registered successfully",
                                "Please. check your email for confirmation"
                        )
                );
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(){
        String jwtToken = jwt.generateJwtTokenForCurrentUser();
        return ResponseEntity.ok(
                new RefreshTokenResponse(
                        "Your token has been refreshed successfully",
                        jwtToken
                )
        );
    }

    @GetMapping("/mail/confirm")
    public ResponseEntity<EmailConfirmationResponse> confirmMail(@RequestParam String token){
        UsersEntity user = email.confirmUserEmail(token);

        if (user.getRole().getRoleName().equals("USER")) {
            String jwtToken = jwt.generateJwtTokenForCurrentUser();

            return ResponseEntity.ok(
                    new EmailConfirmationResponse(
                            "Your email has been successfully confirmed",
                            jwtToken
                    )
            );
        }
        return ResponseEntity.ok(
                new EmailConfirmationResponse(
                        "Your email has been successfully confirmed. Your request has been added",
                        "You will get your token after admin confirmation"
                )
        );
    }

    @PatchMapping("/admin/request")
    public ResponseEntity<ConfirmAdminRequestResponse> confirmAdminRequest(@RequestParam String token, @RequestBody @Valid AdminRequestActionRequest adminAction){
        adminRequestService.handleAdminRequest(token, adminAction);

        return ResponseEntity.ok().body(
                new ConfirmAdminRequestResponse(
                    "Request information has successfully changed"
                )
        );
    }
}
