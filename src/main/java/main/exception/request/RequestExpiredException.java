package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RequestExpiredException extends AppException {
    public RequestExpiredException() {
        super(
                HttpStatus.GONE,
                "Admin request has been expired"
        );
    }
}
