package mk.finki.ukim.lab2.model.exceptions;

public class InvalidUserCredentialsException extends RuntimeException {
    public InvalidUserCredentialsException() {
        super("Invalid user credentials exception");
    }
}
