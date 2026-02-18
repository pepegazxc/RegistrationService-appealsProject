package main.exception;

public class SigningKeyException extends IllegalStateException {
    public SigningKeyException(String message) {
        super(message);
    }
}
