package main.service;

import lombok.extern.slf4j.Slf4j;
import main.dto.request.UserRequest;
import main.entity.UsersEntity;
import main.exception.UserNotFoundException;
import main.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final PasswordEncoder encoder;
    private final UserIdentifierService userIdentifier;

    public UserService(UserRepository userRepository, CipherService cipher, PasswordEncoder encoder, UserIdentifierService userIdentifier) {
        this.userRepository = userRepository;
        this.cipher = cipher;
        this.encoder = encoder;
        this.userIdentifier = userIdentifier;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String cipherEmail = cipher.encrypt(email);

        UsersEntity user = findByCipherEmail(cipherEmail);

        log.debug("User details loaded for identifier {}", user.getUserIdentifier());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserIdentifier())
                .password(user.getHashPassword())
                .authorities(user.getRoles())
                .build();
    }

    @Transactional
    public void registration(UserRequest request){

        UsersEntity user = addNewUser(request);

        userRepository.save(user);
        log.info("New user has been registered. Unique identifier {}", user.getUserIdentifier());
    }

    private String generateUserIdentifier(){
        String identifier;
        do{
            identifier = userIdentifier.generate();
        } while (userRepository.existsByUserIdentifier(identifier));
        return identifier;
    }
    
    private UsersEntity findByCipherEmail(String cipherEmail){
        return userRepository.findByCipherEmail(cipherEmail)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: User not found");
                    return new UserNotFoundException();
                });
    }

    private UsersEntity addNewUser(UserRequest request){
        return new UsersEntity.Builder()
                .name(request.getName())
                .surname(request.getSurname())
                .userIdentifier(generateUserIdentifier())
                .cipherEmail(cipher.encrypt(request.getEmail()))
                .cipherPhoneNumber(cipher.encrypt(request.getPhoneNumber()))
                .hashPassword(encoder.encode(request.getPassword()))
                .roles("ROLE_USER")
                .build();
    }

}
