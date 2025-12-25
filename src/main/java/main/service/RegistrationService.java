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
    private final CipherService cipher;

    public RegistrationService(RegistrationUserRepository registrationRepository, CipherService cipher) {
        this.registrationRepository = registrationRepository;
        this.cipher = cipher;
    }

    @Transactional
    public void registration(UserRequest request){
        UsersEntity user = new UsersEntity.Builder()
                .name(request.getName())
                .surname(request.getSurname())
                .userIdentifier(null)
                .cipherEmail(cipher.encrypt(request.getEmail()))
                .cipherPhoneNumber(cipher.encrypt(request.getPhoneNumber()))
                .hashPassword(request.getPassword())
                .build();
    }
}
