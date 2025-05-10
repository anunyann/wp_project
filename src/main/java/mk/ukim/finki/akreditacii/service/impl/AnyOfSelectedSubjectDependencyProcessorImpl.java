package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidDependencyException;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;
import mk.ukim.finki.akreditacii.repository.SubjectRepository;
import mk.ukim.finki.akreditacii.service.specifications.SubjectDependencyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AnyOfSelectedSubjectDependencyProcessorImpl implements SubjectDependencyProcessor {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public boolean applicableTo(SubjectDependencyType type) {
        return SubjectDependencyType.ANY_OF_SELECTED.equals(type);
    }

    @Override
    public void validate(String dependency) throws SubjectValidationException {
        if (dependency == null || dependency.trim().isEmpty()) {
            throw new SubjectValidationException("ANY_OF_SELECTED dependency cannot be empty");
        }

        if (!dependency.startsWith("ANY_OF_SELECTED:")) {
            throw new SubjectValidationException("Invalid ANY_OF_SELECTED format: " + dependency);
        }

        String subjectCodesStr = dependency.substring("ANY_OF_SELECTED:".length());
        List<String> subjectCodes = Arrays.stream(subjectCodesStr.split(";"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (subjectCodes.isEmpty()) {
            throw new SubjectValidationException("ANY_OF_SELECTED must include at least one subject code");
        }

        for (String code : subjectCodes) {
            if (!Pattern.matches("[A-Z0-9]{3,9}", code)) {
                throw new SubjectValidationException("Invalid subject code format: " + code);
            }

            if (!subjectRepository.existsById(code)) {
                throw new SubjectValidationException("Subject with code does not exist: " + code, code);
            }
        }
    }

    @Override
    public boolean isSatisfied(String dependency, List<String> subjectCodesPassedByStudent)
            throws InvalidDependencyException {
        if (dependency == null || dependency.trim().isEmpty()) {
            return true; //
        }


        String subjectCodesStr = dependency.substring("ANY_OF_SELECTED:".length());
        List<String> requiredCodes = Arrays.stream(subjectCodesStr.split(";"))
                .map(String::trim)
                .collect(Collectors.toList());


        boolean anyPassed = false;
        for (String code : requiredCodes) {
            if (subjectCodesPassedByStudent.contains(code)) {
                anyPassed = true;
                break;
            }
        }

        if (!anyPassed) {
            throw new InvalidDependencyException(
                    "ANY_OF_SELECTED not satisfied",
                    "You need to pass at least one of these subjects: " + String.join(", ", requiredCodes)
            );
        }

        return true;
    }
}