package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import main.dto.AuthResponse;
import main.dto.UserRequest;
import main.service.AuthService;
import main.service.RegistrationService;
import main.service.jwt.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    private final RegistrationService registrationService;
    private final AuthService auth;
    private final AuthTokenService jwt;

    public UserController(RegistrationService registrationService, AuthService auth, AuthTokenService jwt) {
        this.registrationService = registrationService;
        this.auth = auth;
        this.jwt = jwt;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest){
        log.info("Registration attempt email={}", request.getEmail());
        registrationService.registration(request);
        auth.autoAuth(request.getEmail(), request.getPassword(), httpRequest);

        String token = jwt.generateJwtTokenForCurrentUser();

        log.info("Registration success email={}", request.getEmail());
        return ResponseEntity.ok(
                        new AuthResponse(
                                "User has registered successfully",
                                token
                        )
                );
    }
}
