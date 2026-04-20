package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class MayorRequestStatusNotFoundException extends AppException {
    public MayorRequestStatusNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Mayor request status not found"
        );
    }
}
