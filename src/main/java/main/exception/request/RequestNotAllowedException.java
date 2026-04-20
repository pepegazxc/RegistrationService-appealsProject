package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RequestNotAllowedException extends AppException {
    public RequestNotAllowedException() {
        super(
                HttpStatus.NOT_MODIFIED,
                "You cannot submit another request until your first one is rejected or expires"
        );
    }
}
