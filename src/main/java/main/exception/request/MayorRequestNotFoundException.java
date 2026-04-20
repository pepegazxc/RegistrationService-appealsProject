package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class MayorRequestNotFoundException extends AppException {
    public MayorRequestNotFoundException( ) {
        super(
                HttpStatus.NOT_FOUND,
                "Mayor request not found"
        );
    }
}
