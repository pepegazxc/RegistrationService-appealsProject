package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import main.dto.response.*;
import main.dto.request.UserRequest;
import main.service.application.*;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    private final RegistrationService registrationService;
    private final AuthService auth;
    private final AuthTokenService jwt;
    private final EmailConfirmationResultService emailConfirmationResultService;


    public UserController(RegistrationService registrationService, AuthService auth, AuthTokenService jwt, EmailConfirmationResultService emailConfirmationResultService) {
        this.registrationService = registrationService;
        this.auth = auth;
        this.jwt = jwt;
        this.emailConfirmationResultService = emailConfirmationResultService;
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

    @GetMapping("/token/refresh")
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
        EmailConfirmResultResponse result = emailConfirmationResultService.confirmationResult(token);

        return ResponseEntity.ok(
                new EmailConfirmationResponse(
                        result.getMessage(),
                        result.getToken()
                )
        );
    }


}
