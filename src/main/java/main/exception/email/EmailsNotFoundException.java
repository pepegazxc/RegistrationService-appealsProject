package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class EmailsNotFoundException extends AppException {
    public EmailsNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Admins emails not found"
        );
    }
}
