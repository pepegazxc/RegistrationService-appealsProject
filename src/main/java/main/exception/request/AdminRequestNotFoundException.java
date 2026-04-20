package main.exception.request;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class AdminRequestNotFoundException extends AppException {
    public AdminRequestNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Admin request not found. Please check correctness of data entry"
        );
    }
}
