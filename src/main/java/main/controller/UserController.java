package main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import main.dto.UserRequest;
import main.service.AuthService;
import main.service.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final RegistrationService registrationService;
    private final AuthService auth;

    public UserController(RegistrationService registrationService, AuthService auth) {
        this.registrationService = registrationService;
        this.auth = auth;
    }

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest){
        registrationService.registration(request);
        auth.autoAuth(request.getEmail(), request.getPassword(), httpRequest);
        return "User has been registered!";
    }
}
