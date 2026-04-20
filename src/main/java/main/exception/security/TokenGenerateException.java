package main.exception.security;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class TokenGenerateException extends AppException {
    public TokenGenerateException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to generate JWT token. Please try again."
        );
    }
}
