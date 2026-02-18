package main.advice;

import main.dto.response.ExceptionResponse;
import main.exception.EmptySecurityContext;
import main.exception.NotSupportedPrincipal;
import main.exception.SigningKeyException;
import main.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptySecurityContext.class)
    public ResponseEntity<ExceptionResponse> handleEmptySecurityContext(EmptySecurityContext ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                "Security context is empty.Pleas log in again",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(NotSupportedPrincipal.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedPrincipal(NotSupportedPrincipal ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                "Principal is not supported.Please log in again",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(SigningKeyException.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedPrincipal(SigningKeyException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                "Failed to generate JWT token. Please try again.",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedPrincipal(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                "Invalid email or password.",
                ex.getMessage()
        ));
    }
}
