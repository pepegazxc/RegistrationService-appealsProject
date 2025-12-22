package main.controller;

import jakarta.validation.Valid;
import main.dto.UserRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/registration")
    public String registration(@Valid @RequestBody UserRequest request){
        return "User has been registered!";
    }
}
