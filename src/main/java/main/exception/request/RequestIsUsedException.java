package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RequestIsUsedException extends AppException {
    public RequestIsUsedException( ) {
        super(
                HttpStatus.CONFLICT,
                "Admin request is already used"
        );
    }
}
