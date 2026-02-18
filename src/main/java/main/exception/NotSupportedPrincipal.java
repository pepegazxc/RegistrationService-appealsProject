package main.exception;

public class NotSupportedPrincipal extends IllegalStateException {
    public NotSupportedPrincipal(String message) {
        super(message);
    }
}
