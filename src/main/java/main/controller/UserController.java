package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import main.dto.response.AuthResponse;
import main.dto.response.EmailConfirmationResponse;
import main.dto.response.RefreshTokenResponse;
import main.dto.request.UserRequest;
import main.service.AuthService;
import main.service.EmailVerificationService;
import main.service.UserService;
import main.service.jwt.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService auth;
    private final AuthTokenService jwt;
    private final EmailVerificationService email;

    public UserController(UserService userService, AuthService auth, AuthTokenService jwt, EmailVerificationService email) {
        this.userService = userService;
        this.auth = auth;
        this.jwt = jwt;
        this.email = email;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest){
        log.info("Registration attempt email={}", request.getEmail());
        userService.registration(request);
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
        email.confirmUserEmail(token);
        String jwtToken = jwt.generateJwtTokenForCurrentUser();
        return ResponseEntity.ok(
                new EmailConfirmationResponse(
                        "Your email has been successfully confirmed",
                        jwtToken
                )
        );
    }

    @GetMapping("")
    public ResponseEntity confirmMailForAdmin(){
        return null;
    }
}
