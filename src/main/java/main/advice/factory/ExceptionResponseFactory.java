package main.advice.factory;

import main.dto.response.ExceptionResponse;
import main.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExceptionResponseFactory {

    public ResponseEntity<ExceptionResponse> build(AppException ex){
            return ResponseEntity
                    .status(ex.status())
                    .body(new ExceptionResponse(ex.publicMessage()));
    }

    public ResponseEntity<ExceptionResponse> build(HttpStatus status, String message){
        return ResponseEntity
                .status(status)
                .body(new ExceptionResponse(message));
    }
}
