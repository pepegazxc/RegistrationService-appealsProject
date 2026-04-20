package main.advice;

import main.advice.factory.ExceptionResponseFactory;
import main.advice.mapper.ConstraintViolationMapper;
import main.dto.response.ExceptionResponse;
import main.exception.AppException;
import main.exception.user.RegistrationFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionResponseFactory factory;
    private final ConstraintViolationMapper mapper;

    public GlobalExceptionHandler(ExceptionResponseFactory factory, ConstraintViolationMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
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
            AppException exception = mapper.map(constraint.getConstraintName());

            return factory.build(exception);
        }

        return factory.build(new RegistrationFailedException());
    }

}
