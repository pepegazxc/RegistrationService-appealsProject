package main.exception.email;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class AdminsEmailsNotFoundException extends AppException {
    public AdminsEmailsNotFoundException() {
        super(
                HttpStatus.NOT_FOUND,
                "Admins emails not found"
        );
    }
}
