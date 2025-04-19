package mk.ukim.finki.akreditacii.model.exceptions;

public class EmailAlreadyExists extends RuntimeException{
    public EmailAlreadyExists(String message) {
        super(message);
    }
}
