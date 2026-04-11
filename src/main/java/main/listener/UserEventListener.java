package main.listener;

import main.event.AdminRequestEvent;
import main.event.MayorRequestEvent;
import main.event.RegistrationEvent;
import main.event.RequestResponseEvent;
import main.service.AdminRequestResponseResultService;
import main.service.MayorRequestResponseResultService;
import main.service.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserEventListener {

    @Value("${app.url}")
    private String appUrl;
    @Value("${mail.main}")
    private String mainMail;

    private final MailService mailService;
    private final AdminRequestResponseResultService adminResultService;
    private final MayorRequestResponseResultService mayorRequestResultService;

    public UserEventListener(MailService mailService, AdminRequestResponseResultService adminResultService, MayorRequestResponseResultService mayorRequestResultService) {
        this.mailService = mailService;
        this.adminResultService = adminResultService;
        this.mayorRequestResultService = mayorRequestResultService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMail(RegistrationEvent userEvent){
        String link = appUrl + "/mail/confirm?token=" + userEvent.getToken();

        mailService.sendMail(
                userEvent.getEmail(),
                "Mail confirmation",
                "Please use this link for confirm your mail: " + link
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAdminRequestMail(AdminRequestEvent adminEvent){
        String link = appUrl + "/admin/request?token=" + adminEvent.getToken();

        mailService.sendMail(
                mainMail,
                "Admin confirmation",
                "Use this link confirm or reject admin: " + link
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMayorRequestMail(MayorRequestEvent mayorRequestEvent) {
        String link = appUrl + "/mayor/request?token=" + mayorRequestEvent.getToken();

        mailService.sendMail(
                mayorRequestEvent.getAdminEmail(),
                "Mayor confirmation",
                "Use this link to confirm or reject the mayor request: " + link
        );
    }

    @EventListener
    public void handleAdminRequestResponseMail(RequestResponseEvent adminResponseEvent){
        mailService.sendMail(
                adminResponseEvent.getEmail(),
                "Admin request results",
                adminResultService.handleAdminRequestResult(adminResponseEvent.getAction())
        );
    }

    @EventListener
    public void handleMayorRequestResponseMail(RequestResponseEvent mayorResponseEvent){
        mailService.sendMail(
                mayorResponseEvent.getEmail(),
                "Mayor request results",
                mayorRequestResultService.handleMayorRequestResult(mayorResponseEvent.getAction())
        );
    }
}
