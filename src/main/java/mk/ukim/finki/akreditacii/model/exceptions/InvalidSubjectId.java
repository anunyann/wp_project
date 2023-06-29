package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidSubjectId extends RuntimeException {

    public InvalidSubjectId(String subjectId) {
        super("Invalid subject id: "+subjectId);
    }
}
