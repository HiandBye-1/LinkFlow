package Jar.exception;

public class ExceedAttemptsLimitException extends RuntimeException {
    public ExceedAttemptsLimitException(String message) {
        super(message);
    }
    
}
