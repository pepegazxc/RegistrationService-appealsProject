package main.exception.cryptography;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class EncryptException extends AppException {
    public EncryptException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while encrypting"
        );
    }
}
