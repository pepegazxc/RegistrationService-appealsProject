package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import main.dto.response.AuthResponse;
import main.dto.response.RefreshTokenResponse;
import main.dto.request.UserRequest;
import main.service.AuthService;
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

    public UserController(UserService userService, AuthService auth, AuthTokenService jwt) {
        this.userService = userService;
        this.auth = auth;
        this.jwt = jwt;
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
        String token = jwt.generateJwtTokenForCurrentUser();
        return ResponseEntity.ok(
                new RefreshTokenResponse(
                        "Your token has been refreshed successfully",
                        token
                )
        );
    }

    @PostMapping("/mail/confirm?token=")
    public void confirmMail(@RequestParam String token){

    }
}
