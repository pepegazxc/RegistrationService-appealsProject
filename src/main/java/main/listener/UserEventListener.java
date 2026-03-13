package main.listener;

import main.event.UserRegisteredEvent;
import main.service.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Component;


@Component
public class UserEventListener {

    @Value("${app.url}")
    private String appUrl;

    private final MailService mailService;

    public UserEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    @EventListener
    public void handleMail(UserRegisteredEvent userEvent){
        String link = appUrl + "/mail/confirm?token=" + userEvent.getToken();

        mailService.sendMail(
                userEvent.getEmail(),
                "Mail confirmation",
                "Please use this link for confirm your mail: " + link
        );
    }
}
