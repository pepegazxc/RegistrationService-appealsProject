package main.service;

import main.dto.UserRequest;
import main.entity.UsersEntity;
import main.repository.RegistrationUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class RegistrationService {

    private final RegistrationUserRepository registrationRepository;

    public RegistrationService(RegistrationUserRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public void registration(UserRequest request){
        UsersEntity user = new UsersEntity.Builder()
                .name(request.getName())
                .surname(request.getSurname())
                .userIdentifier(null)
                .cipherEmail(request.getEmail())
                .cipherPhoneNumber(request.getPhoneNumber())
                .hashPassword(request.getPassword())
                .build();
    }
}
