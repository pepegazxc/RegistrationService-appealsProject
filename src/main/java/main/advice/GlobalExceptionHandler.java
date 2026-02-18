package main.advice;

import main.dto.response.ExceptionResponse;
import main.exception.*;
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

    @ExceptionHandler(TokenGenerateException.class)
    public ResponseEntity<ExceptionResponse> handleNotSupportedPrincipal(TokenGenerateException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                "Failed to generate JWT token. Please try again.",
                ex.getMessage()
        ));
    }
}
