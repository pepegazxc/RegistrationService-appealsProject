package main.advice;

import main.advice.factory.ExceptionResponseFactory;
import main.dto.response.ExceptionResponse;
import main.exception.AppException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionResponseFactory factory;

    public GlobalExceptionHandler(ExceptionResponseFactory factory) {
        this.factory = factory;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handle(AppException ex){
        return factory.build(ex);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ExceptionResponse> handeMailSend(MailSendException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        "Something went wrong while mail sending"
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIntegrityException(DataIntegrityViolationException ex){
        Throwable cause = ex.getCause();

        if (cause instanceof org.hibernate.exception.ConstraintViolationException constraint){

            String sqlState = constraint.getSQLException().getSQLState();
            String constraintName = constraint.getConstraintName();

            if ("23505".equals(sqlState)) {
                return handleConstraint(constraintName, ex);
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        "An error occurred during registration. Please try to registering again.")
                );
    }



    private ResponseEntity<ExceptionResponse> handleConstraint(String constraintName, DataIntegrityViolationException ex){
        if ("users_cipher_email_key".equals(constraintName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ExceptionResponse(
                            "User with that email exists."
                    ));
        }
        if ("users_cipher_phone_number_key".equals(constraintName)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                    "User with that phone already exist. Please enter another phone number."
            ));
        }
        if ("users_identifier_key".equals(constraintName)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                    "Failed to generate unique user identifier. Please try again."
            ));
        }

        if ("one_admin_request_per_user".equals(constraintName)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ExceptionResponse(
                    "You cannot submit another request until your first one is rejected or expires"
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        "An error occurred during registration. Please try to registering again."
                ));
    }
}
