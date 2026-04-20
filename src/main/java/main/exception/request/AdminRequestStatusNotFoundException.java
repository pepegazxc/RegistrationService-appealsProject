package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class AdminRequestStatusNotFoundException extends AppException {
    public AdminRequestStatusNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Admin request not found. Please check correctness of data entry"
        );
    }
}
