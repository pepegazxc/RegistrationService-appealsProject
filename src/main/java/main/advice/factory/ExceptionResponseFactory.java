package main.advice.factory;

import main.dto.response.ExceptionResponse;
import main.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExceptionResponseFactory {

    public ResponseEntity<ExceptionResponse> build(AppException ex){
            return ResponseEntity
                    .status(ex.status())
                    .body(new ExceptionResponse(ex.publicMessage()));
    }
}
