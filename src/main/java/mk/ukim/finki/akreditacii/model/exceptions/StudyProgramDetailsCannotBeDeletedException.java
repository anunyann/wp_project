package mk.ukim.finki.akreditacii.model.exceptions;

public class StudyProgramDetailsCannotBeDeletedException extends RuntimeException {

    public StudyProgramDetailsCannotBeDeletedException(String id) {
        super("Study Program Details with id: " + id + " cannot be deleted");
    }
}
