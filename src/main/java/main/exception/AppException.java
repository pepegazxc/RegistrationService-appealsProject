package main.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String publicMessage;


    protected AppException(HttpStatus status, String publicMessage) {
        this.status = status;
        this.publicMessage = publicMessage;
    }

    public HttpStatus status() {return status;}
    public String publicMessage() {return  publicMessage;}
}
