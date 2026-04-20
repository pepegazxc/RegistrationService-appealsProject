package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class ExpiredEmailTokenException extends AppException {
    public ExpiredEmailTokenException() {
        super(
                HttpStatus.GONE,
                "Your email verification token has expired. Please delete your account and register again"
        );
    }
}
