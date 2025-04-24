package mk.ukim.finki.akreditacii.model.exceptions;

public class InvalidDependencyException extends Exception {
    private String reason;

    public InvalidDependencyException(String message) {
        super(message);
    }

    public InvalidDependencyException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}