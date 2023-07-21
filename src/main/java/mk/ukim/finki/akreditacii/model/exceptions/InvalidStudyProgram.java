package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidStudyProgram extends RuntimeException {

    public InvalidStudyProgram(String id) {
        super("Invalid study program id: "+id);
    }
}
