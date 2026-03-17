package main.advice;

import main.dto.response.ExceptionResponse;
import main.exception.security.EmptySecurityContextException;
import main.exception.security.NotSupportedPrincipalException;
import main.exception.security.SigningKeyException;
import main.exception.security.TokenGenerateException;
import main.exception.user.RoleNotFoundException;
import main.exception.user.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptySecurityContextException.class)
    public ResponseEntity<ExceptionResponse> handleEmptySecurityContext(EmptySecurityContextException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                "Security context is empty.Pleas log in again",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(NotSupportedPrincipalException.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedPrincipal(NotSupportedPrincipalException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                "Principal is not supported.Please log in again",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(SigningKeyException.class)
    public ResponseEntity<ExceptionResponse> handleSigningKeyException(SigningKeyException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                "Failed to generate JWT token. Please try again.",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                "Invalid email or password.",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(TokenGenerateException.class)
    public ResponseEntity<ExceptionResponse> handleTokenGenerateException(TokenGenerateException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                "Failed to generate JWT token. Please try again.",
                ex.getMessage()
        ));
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
                        "An error occurred during registration. Please try to registering again.",
                        ex.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoleNotFound(RoleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(
                        "Role not found. Please choose user, admnin or mayor",
                        ex.getMessage()
                ));
    }

    private ResponseEntity<ExceptionResponse> handleConstraint(String constraintName, DataIntegrityViolationException ex){
        if ("users_cipher_email_key".equals(constraintName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ExceptionResponse(
                            "User with that email exists.",
                            ex.getMessage()));
        }
        if ("users_cipher_phone_number_key".equals(constraintName)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                    "User with that phone already exist. Please enter another phone number.",
                    ex.getMessage()
            ));
        }
        if ("users_identifier_key".equals(constraintName)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                    "Failed to generate unique user identifier. Please try again.",
                    ex.getMessage()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(
                        "An error occurred during registration. Please try to registering again.",
                        ex.getMessage()));
    }


}
