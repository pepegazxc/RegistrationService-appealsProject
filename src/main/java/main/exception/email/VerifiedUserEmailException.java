package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class VerifiedUserEmailException extends AppException {
    public VerifiedUserEmailException() {
        super(
                HttpStatus.NOT_MODIFIED,
                "Your email has already been verified"
        );
    }
}
