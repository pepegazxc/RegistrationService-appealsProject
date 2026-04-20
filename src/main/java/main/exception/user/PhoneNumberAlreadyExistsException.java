package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class PhoneNumberAlreadyExistsException extends AppException {
    public PhoneNumberAlreadyExistsException( ) {
        super(
                HttpStatus.CONFLICT,
                "User with that phone number already exist."
        );
    }
}
