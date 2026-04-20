package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RegistrationFailedException extends AppException {
    public RegistrationFailedException( ) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred during registration. Please try to registering again."
        );
    }
}
