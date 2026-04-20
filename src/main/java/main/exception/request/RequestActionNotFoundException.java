package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RequestActionNotFoundException extends AppException {
    public RequestActionNotFoundException( ) {
        super(
                HttpStatus.NOT_FOUND,
                "Wrong action to request. Choose between REJECTED and APPROVED"
        );
    }
}
