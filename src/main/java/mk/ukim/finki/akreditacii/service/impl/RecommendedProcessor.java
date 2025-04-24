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
public class RecommendedProcessor implements SubjectDependencyProcessor {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public boolean applicableTo(SubjectDependencyType type) {
        return SubjectDependencyType.RECOMMENDED.equals(type);
    }

    @Override
    public void validate(String dependency) throws SubjectValidationException {
        if (dependency == null || dependency.trim().isEmpty()) {
            return; // Empty recommended is valid
        }

        // Extract subject codes and validate they exist
        List<String> subjectCodes = Arrays.stream(dependency.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        for (String code : subjectCodes) {
            if (!Pattern.matches("[A-Z0-9]{6,8}", code)) {
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
        // Recommended subjects are not required, so always return true
        return true;
    }
}