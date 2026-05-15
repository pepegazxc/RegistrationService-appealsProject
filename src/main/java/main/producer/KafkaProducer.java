package main.producer;

import lombok.RequiredArgsConstructor;
import main.dto.enums.RequestsActionEnum;
import main.event.*;
import main.service.application.RequestResponseResultService;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, MailEvent> kafka;
    private final RequestResponseResultService resultService;

    public void handleRegistration(String email, String token){
        RegistrationEvent event =  new RegistrationEvent();
        build(event,email, token);
        kafka.send("user-registered", event);

    }

    public void handleAdminRequestMail(String token){
        AdminRequestEvent event = new AdminRequestEvent();
        event.setToken(token);
        kafka.send("admin-request", event);
    }

    public void handleMayorRequestMail(String email, String token) {
        MayorRequestEvent event = new MayorRequestEvent();
        build(event,email, token);
        kafka.send("mayor-request", event);
    }

    public void handleRequestResponseMail(String email, RequestsActionEnum action){
        RequestResponseEvent event = new RequestResponseEvent();
        build(event,email);
        event.setMessage(resultService.handleRequestResult(action));
        kafka.send("request-response", event);
    }

    public void handleLoginMail(String email){
        LoginEvent event = new LoginEvent();
        build(event,email);
        event.setTime(LocalDateTime.now());
        kafka.send("user-logged", event);
    }

    private void build(MailEvent event, String email){
        event.setEmail(email);
    }
    private void build(MailEvent event, String email, String token){
        event.setEmail(email);
        event.setToken(token);
    }
}
