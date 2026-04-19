package main.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String message;


    protected AppException(String details, HttpStatus status, String message) {
        super(details);
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {return status;}
    public String message() {return  message;}
}
