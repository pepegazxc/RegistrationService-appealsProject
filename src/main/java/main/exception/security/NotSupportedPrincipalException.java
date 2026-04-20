package main.exception.security;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class NotSupportedPrincipalException extends AppException {
    public NotSupportedPrincipalException() {
        super(
                HttpStatus.UNAUTHORIZED,
                "Principal is not supported.Please log in again"
        );
    }
}
