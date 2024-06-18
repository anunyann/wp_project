package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidProfessorId extends RuntimeException{
    public InvalidProfessorId(String professorId) {super("Invalid subject id: "+professorId);}
}
