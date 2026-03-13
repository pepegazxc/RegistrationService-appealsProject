package main.service.mail;

public interface MailSender {
    void SendMail(String to, String subject, String text);
}
