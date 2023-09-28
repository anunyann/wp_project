package mk.ukim.finki.akreditacii.model.exceptions;

public class NoActiveAccreditation extends RuntimeException{
    public NoActiveAccreditation(String id) {
        super("No active accreditation");
    }
}
