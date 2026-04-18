package main.advice.request;

import main.dto.response.ExceptionResponse;
import main.exception.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdminRequestExceptionHandler {

    @ExceptionHandler(RequestActionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAdminRequestActionException(RequestActionNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        "Wrong action to request. Choose between REJECTED and APPROVED",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AdminRequestExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleAdminRequestExpired(AdminRequestExpiredException ex){
        return ResponseEntity.status(HttpStatus.GONE).body(
                new ExceptionResponse(
                        "Admin request has been expired",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AdminRequestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAdminRequestNotFound (AdminRequestNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        "Admin request not found. Please check correctness of data entry",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AdminRequestStatusNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAdminRequestStatusNotFound (AdminRequestStatusNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        "Admin request status not found. Please check correctness of data entry",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AdminRequestTokenIsUsedException.class)
    public ResponseEntity<ExceptionResponse> handleUsedAdminRequestToken (AdminRequestTokenIsUsedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        "Admin request token is already used",
                        ex.getMessage()
                )
        );
    }
}

