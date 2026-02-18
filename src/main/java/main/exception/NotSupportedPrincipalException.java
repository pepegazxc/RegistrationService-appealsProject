package main.exception;

public class NotSupportedPrincipalException extends IllegalStateException {
    public NotSupportedPrincipalException(String message) {
        super(message);
    }
}
