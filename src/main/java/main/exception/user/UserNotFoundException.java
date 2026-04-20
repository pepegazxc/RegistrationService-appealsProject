package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {
    public UserNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Invalid email or password."
        );
    }
}
