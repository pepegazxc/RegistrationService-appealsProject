package main.listener;

import main.event.AdminRequestEvent;
import main.event.RegistrationEvent;
import main.service.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Component;


@Component
public class UserEventListener {

    @Value("${app.url}")
    private String appUrl;
    @Value("${mail.main}")
    private String mainMail;

    private final MailService mailService;

    public UserEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    @EventListener
    public void handleMail(RegistrationEvent userEvent){
        String link = appUrl + "/mail/confirm?token=" + userEvent.getToken();

        mailService.sendMail(
                userEvent.getEmail(),
                "Mail confirmation",
                "Please use this link for confirm your mail: " + link
        );
    }

    @Async
    @EventListener
    public void handleAdminRequestMail(AdminRequestEvent adminEvent){
        String link = appUrl + "/mail/confirm?token=" + adminEvent.getToken();

        mailService.sendMail(
                mainMail,
                "Admin confirmation",
                "Use this link confirm or reject admin: " + link
        );
    }
}
