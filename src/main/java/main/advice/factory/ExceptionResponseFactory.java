package main.advice.factory;

import main.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExceptionResponseFactory {

    public ResponseEntity<ExceptionResponse> buildResponse(HttpStatus status, String message, String ex){
        return ResponseEntity.status(status).body(
                new ExceptionResponse(
                        message,
                        ex
                )
        );

    }
}
