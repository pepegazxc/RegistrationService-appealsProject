package main.service;

import main.event.MayorRequestEvent;
import main.repository.UserRepository;
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
        for (String e : getAdminsEmails()) {
            String email = e;

            publisher.publishEvent(
                    new MayorRequestEvent(
                            email,
                            mayorToken
                    )
            );
        }
    }

    private List<String> getAdminsEmails(){
        List<String> emails = userRepository.selectAdminsEmails().
                stream().map(cipher::decrypt).toList();

        return emails;
    }
}
