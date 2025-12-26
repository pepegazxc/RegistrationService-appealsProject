package main.service;

import main.dto.UserRequest;
import main.entity.UsersEntity;
import main.repository.RegistrationUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class RegistrationService {

    private final RegistrationUserRepository registrationRepository;
    private final CipherService cipher;
    private final PasswordEncoder encoder;
    private final UserIdentifierService userIdentifier;

    public RegistrationService(RegistrationUserRepository registrationRepository, CipherService cipher, PasswordEncoder encoder, UserIdentifierService userIdentifier) {
        this.registrationRepository = registrationRepository;
        this.cipher = cipher;
        this.encoder = encoder;
        this.userIdentifier = userIdentifier;
    }

    @Transactional
    public void registration(UserRequest request){
        UsersEntity user = new UsersEntity.Builder()
                .name(request.getName())
                .surname(request.getSurname())
                .userIdentifier(generateUserIdentifier())
                .cipherEmail(cipher.encrypt(request.getEmail()))
                .cipherPhoneNumber(cipher.encrypt(request.getPhoneNumber()))
                .hashPassword(encoder.encode(request.getPassword()))
                .build();

        registrationRepository.save(user);
    }

    private String generateUserIdentifier(){
        String identifier;
        do{
            identifier = userIdentifier.generate();
        } while (registrationRepository.existsByUserIdentifier(identifier));
        return identifier;
    }
}
