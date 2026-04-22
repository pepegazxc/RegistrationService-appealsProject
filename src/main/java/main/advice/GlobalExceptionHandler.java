package main.advice;

import lombok.extern.slf4j.Slf4j;
import main.advice.factory.ExceptionResponseFactory;
import main.advice.mapper.ConstraintViolationMapper;
import main.dto.response.ExceptionResponse;
import main.exception.AppException;
import main.exception.user.RegistrationFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionResponseFactory factory;
    private final ConstraintViolationMapper mapper;

    public GlobalExceptionHandler(ExceptionResponseFactory factory, ConstraintViolationMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handle(AppException ex){
        log.warn("Business exception handled [{}]:  {}", ex.getClass().getSimpleName(), ex.getMessage());
        return factory.build(ex);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ExceptionResponse> handeMailSend(MailSendException ex){
        log.error("Mail sending failed", ex);
        return factory.build(
                INTERNAL_SERVER_ERROR,
                "Something went wrong while mail sending"
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleMessageNotReadableException(HttpMessageNotReadableException ex){
        log.warn("Invalid request body received: {}", ex.getMessage());
        return factory.build(
                BAD_REQUEST,
                "Wrong role. Available values: user, admin, mayor"
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        FieldError error = ex.getBindingResult().getFieldError();
        String message = error != null ? error.getDefaultMessage() : "Validation failed";
        log.warn("Validation failed for fields '{}': {}", error.getField(), message);

        return factory.build(
                BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIntegrityException(DataIntegrityViolationException ex){
        Throwable cause = ex.getCause();

        if (cause instanceof org.hibernate.exception.ConstraintViolationException constraint){
            AppException exception = mapper.map(constraint.getConstraintName());

            log.warn("Business exception handled '{}' : {}", exception.getClass().getSimpleName(),exception.getMessage() );
            return factory.build(exception);
        }

        log.error("Unhandled data integrity violation", ex);
        return factory.build(new RegistrationFailedException());
    }

}
