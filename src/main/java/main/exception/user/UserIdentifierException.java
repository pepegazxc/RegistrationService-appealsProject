package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class UserIdentifierException extends AppException {
    public UserIdentifierException( ) {
        super(
                HttpStatus.CONFLICT,
                "Failed to generate unique user identifier. Please try again."
        );
    }
}
