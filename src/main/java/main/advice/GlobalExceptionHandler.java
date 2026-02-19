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

    @ExceptionHandler(UserIdentifierException.class)
    public ResponseEntity<ExceptionResponse> handleUserIdentifierException(UserIdentifierException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                "Failed to generate unique user identifier. Please try again.",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                "User with that phone already exist. Please enter another phone number.",
                ex.getMessage()
        ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                "User with that email  exist. Please enter another email.",
                ex.getMessage()
        ));
    }
}
