package main.advice;

import main.dto.response.ExceptionResponse;
import main.exception.email.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MailExceptionHandler {

    @ExceptionHandler(EmailTokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEmailTokenNotFound(EmailTokenNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        "Verification token for your email not found. Please try again",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ExpiredEmailTokenException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredEmailToken(ExpiredEmailTokenException ex){
        return ResponseEntity.status(HttpStatus.GONE).body(
                new ExceptionResponse(
                        "Your email verification token has expired. Please delete your account and register again",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UsedEmailTokenException.class)
    public ResponseEntity<ExceptionResponse> handleUsedEmailToken(UsedEmailTokenException ex){
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                new ExceptionResponse(
                        "Your email verification token has already been used",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(VerifiedUserEmailException.class)
    public ResponseEntity<ExceptionResponse> handleVerifiedUserEmail(VerifiedUserEmailException ex){
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                new ExceptionResponse(
                        "Your email has already been verified",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ExceptionResponse> handeMailSend(MailSendException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        "Something went wrong while mail sending",
                        ex.getMessage()
                )
        );
    }
}
