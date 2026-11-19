package Jar.exception;

public class WrongShortCodeException extends RuntimeException {
    public WrongShortCodeException(String message) {
        super(message);
    }
}