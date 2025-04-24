package mk.ukim.finki.akreditacii.service.specifications;

import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidDependencyException;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;

import java.util.List;

public interface SubjectDependencyProcessor {
    boolean applicableTo(SubjectDependencyType type);

    void validate(String dependency) throws SubjectValidationException;

    boolean isSatisfied(String dependency, List<String> subjectCodesPassedByStudent)
            throws InvalidDependencyException;
}