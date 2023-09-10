package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidAccreditation extends RuntimeException {
    public InvalidAccreditation(String id) {
        super("Invalid Accreditation id: " + id);
    }
}
