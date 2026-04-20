package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class UsedEmailTokenException extends AppException {
    public UsedEmailTokenException() {
        super(
                HttpStatus.NOT_MODIFIED,
                "Your email verification token has already been used"
        );
    }
}
