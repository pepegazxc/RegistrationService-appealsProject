package main.service;

import main.dto.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    @Transactional
    public void registration(UserRequest request){

    }
}
