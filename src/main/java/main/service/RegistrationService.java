package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.UserRequest;
import main.entity.UsersEntity;
import main.repository.RegistrationUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegistrationService implements UserDetailsService {

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


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String cipherEmail = encoder.encode(email);

        UsersEntity user = registrationRepository.findByCipherEmail(cipherEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserIdentifier())
                .password(user.getHashPassword())
                .authorities("ROLE_USER")
                .build();
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
        log.info("New user {} has been registered. Unique identifier {}", user.getName(), user.getUserIdentifier());
    }

    private String generateUserIdentifier(){
        String identifier;
        do{
            identifier = userIdentifier.generate();
        } while (registrationRepository.existsByUserIdentifier(identifier));
        return identifier;
    }

}
