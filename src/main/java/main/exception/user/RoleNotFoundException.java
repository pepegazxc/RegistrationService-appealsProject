package main.exception.user;

import main.exception.AppException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends AppException {
    public RoleNotFoundException( ) {
        super(
                HttpStatus.NOT_FOUND,
                "Role not found. Please choose user, admin or mayor."
        );
    }
}
