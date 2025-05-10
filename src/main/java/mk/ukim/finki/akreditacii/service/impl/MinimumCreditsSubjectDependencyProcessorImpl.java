package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidDependencyException;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;
import mk.ukim.finki.akreditacii.model.subject.Subject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.SubjectRepository;
import mk.ukim.finki.akreditacii.service.specifications.SubjectDependencyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class MinimumCreditsSubjectDependencyProcessorImpl implements SubjectDependencyProcessor {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public boolean applicableTo(SubjectDependencyType type) {
        return SubjectDependencyType.MINIMUM_CREDITS.equals(type);
    }

    @Override
    public void validate(String dependency) throws SubjectValidationException {
        if (dependency == null || dependency.trim().isEmpty()) {
            throw new SubjectValidationException("Minimum credits dependency cannot be empty");
        }

        // Format validation: Should be in the format "MINIMUM_CREDITS:180ECTS"
        String pattern = "^MINIMUM_CREDITS:(\\d+)ECTS$";
        if (!Pattern.matches(pattern, dependency)) {
            throw new SubjectValidationException("Invalid minimum credits format: " + dependency);
        }

        // Extract the credits value
        String creditsStr = dependency.substring(dependency.indexOf(":") + 1, dependency.indexOf("ECTS"));
        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits <= 0) {
                throw new SubjectValidationException("Credits must be a positive number: " + credits);
            }
        } catch (NumberFormatException e) {
            throw new SubjectValidationException("Invalid credits value: " + creditsStr);
        }
    }

    @Override
    public boolean isSatisfied(String dependency, List<String> subjectCodesPassedByStudent)
            throws InvalidDependencyException {
        if (dependency == null || dependency.trim().isEmpty()) {
            return true; // No minimum credits requirement
        }

        // Extract the required credits
        String creditsStr = dependency.substring(dependency.indexOf(":") + 1, dependency.indexOf("ECTS"));
        int requiredCredits;
        try {
            requiredCredits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            throw new InvalidDependencyException(
                    "Invalid credits format",
                    "The credits value is not a valid number: " + creditsStr
            );
        }

        int totalCredits = calculateTotalCredits(subjectCodesPassedByStudent);

        if (totalCredits < requiredCredits) {
            throw new InvalidDependencyException(
                    "Minimum credits not satisfied",
                    String.format("You need at least %d credits, but you have only %d", requiredCredits, totalCredits)
            );
        }

        return true;
    }

    // This method would need to be implemented to calculate the total credits
    // based on the subjects passed by the student
    private int calculateTotalCredits(List<String> subjectCodesPassedByStudent) {
        if (subjectCodesPassedByStudent == null || subjectCodesPassedByStudent.isEmpty()) {
            return 0;
        }

        return subjectCodesPassedByStudent.size() * 6;
    }
}