package main.exception;

public class EmptySecurityContext extends IllegalStateException {
    public EmptySecurityContext(String message) {
        super(message);
    }
}
