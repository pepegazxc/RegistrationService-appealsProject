package main.service.application;

import main.event.MayorRequestEvent;
import main.exception.email.AdminsEmailsNotFoundException;
import main.repository.UserRepository;
import main.service.infrastructure.CipherService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminsEmailsService {

    private final UserRepository userRepository;
    private final CipherService cipher;
    private final ApplicationEventPublisher publisher;

    public AdminsEmailsService(UserRepository userRepository, CipherService cipher, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.cipher = cipher;
        this.publisher = publisher;
    }

    public void sendMailToAdmins(String mayorToken){
        List<String> emails = getAdminsEmails();

        for(String email : emails) {
            publisher.publishEvent(
                    new MayorRequestEvent(
                            email,
                            mayorToken
                    )
            );
        }
    }

    private List<String> getAdminsEmails(){
        List<String> emails = userRepository.selectAdminsEmails();

        if (emails == null || emails.isEmpty()) throw new AdminsEmailsNotFoundException();

        return emails.stream().map(cipher::decrypt).toList();
    }


}
