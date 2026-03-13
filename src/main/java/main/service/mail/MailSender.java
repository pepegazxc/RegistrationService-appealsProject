package main.service.mail;

public interface MailSender {
    void sendMail(String to, String subject, String text);
}