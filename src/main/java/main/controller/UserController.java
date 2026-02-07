package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import main.dto.UserRequest;
import main.service.AuthService;
import main.service.RegistrationService;
import main.service.jwt.AuthTokenService;
import main.service.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public ResponseEntity<String> registration(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest){
        registrationService.registration(request);
        auth.autoAuth(request.getEmail(), request.getPassword(), httpRequest);

        String token = jwt.generateJwtTokenForCurrentUser();

        return ResponseEntity.ok().body("User has registered successful! \n Token : " + token);
    }
}
