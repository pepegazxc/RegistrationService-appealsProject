package main.service.application;

import lombok.RequiredArgsConstructor;
import main.exception.email.EmailsNotFoundException;
import main.producer.KafkaProducer;
import main.repository.UserRepository;
import main.service.infrastructure.CipherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminsEmailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final KafkaProducer kafka;

    public void sendMailToAdmins(String mayorToken){
        List<String> emails = getAdminsEmails();

        for(String email : emails) {
            kafka.handleMayorRequestMail(
                    email,
                    mayorToken
            );
        }
    }

    private List<String> getAdminsEmails(){
        List<String> emails = userRepository.selectAdminsEmails();

        if (emails == null || emails.isEmpty()) throw new EmailsNotFoundException();

        return emails.stream().map(cipher::decrypt).toList();
    }


}
