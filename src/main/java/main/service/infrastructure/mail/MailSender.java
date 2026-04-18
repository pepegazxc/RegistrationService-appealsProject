package main.service.infrastructure.mail;

public interface MailSender {
    void sendMail(String to, String subject, String text);
}