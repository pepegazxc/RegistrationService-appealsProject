package main.controller;

import lombok.RequiredArgsConstructor;
import main.dto.response.InternalUserDto;
import main.service.application.InternalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InternalController {

    private final InternalService internalService;

    @GetMapping("/internal/users/{userIdentifier}")
    public InternalUserDto getUserEmail(@PathVariable String userIdentifier){
        String email = internalService.getEmail(userIdentifier);
        return new InternalUserDto(email);
    }
}
