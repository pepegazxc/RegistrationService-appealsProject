package main.exception.security;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class EmptySecurityContextException extends AppException {
    public EmptySecurityContextException() {
        super(
                HttpStatus.UNAUTHORIZED,
                "Security context is empty.Pleas log in again"
        );
    }
}
