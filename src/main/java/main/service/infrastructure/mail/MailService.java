package main.service.infrastructure.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService implements MailSender {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String to, String subject, String text) {
        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(to);
                mailMessage.setSubject(subject);
                mailMessage.setText(text);

                mailSender.send(mailMessage);
                return;

            } catch(MailException ex) {
                attempts++;
                log.error("Attempt {} failed to send mail to {}", attempts, to ,ex);

                if (attempts >= maxAttempts) throw new MailSendException("Failed after retries", ex);

                try {
                    Thread.sleep(3000 * attempts);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    throw new MailSendException("Retry interrupted", e);
                }
                }
        }
    }
}
