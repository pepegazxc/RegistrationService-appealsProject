package main.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.entity.UsersEntity;
import main.exception.email.EmailsNotFoundException;
import main.exception.user.UserNotFoundException;
import main.repository.UserRepository;
import main.service.infrastructure.CipherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalService {

    private final UserRepository userRepository;
    private final CipherService cipher;

    public String getEmail(String userIdentifier){
        UsersEntity user = userRepository.findByUserIdentifier(userIdentifier)
                .orElseThrow(() -> new UserNotFoundException());

        return cipher.decrypt(user.getCipherEmail());
    }

    public List<String> getMayorsEmails(){
        List<String> emails = userRepository.selectMayorsEmails();

        if(emails == null || emails.isEmpty()) throw new EmailsNotFoundException();

        return emails.stream().map(cipher::decrypt).toList();
    }

}
