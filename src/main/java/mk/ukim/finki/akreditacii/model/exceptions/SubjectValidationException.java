package mk.ukim.finki.akreditacii.model.exceptions;

public class SubjectValidationException extends Exception {
    private String subjectCode;

    public SubjectValidationException(String message) {
        super(message);
    }

    public SubjectValidationException(String message, String subjectCode) {
        super(message);
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }
}