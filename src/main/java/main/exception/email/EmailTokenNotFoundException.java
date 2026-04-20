package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class EmailTokenNotFoundException extends AppException {
    public EmailTokenNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Verification token for your email not found. Please try again"
        );
    }
}
