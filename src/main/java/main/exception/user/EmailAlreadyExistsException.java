package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException() {
        super(
                HttpStatus.CONFLICT,
                "User with that email exists."
        );
    }
}
