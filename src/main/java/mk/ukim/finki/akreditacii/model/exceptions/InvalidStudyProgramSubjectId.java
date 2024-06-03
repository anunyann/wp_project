package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidStudyProgramSubjectId extends RuntimeException {

    public InvalidStudyProgramSubjectId(String subjectId) {
        super("Invalid study program subject id: "+subjectId);
    }

}
