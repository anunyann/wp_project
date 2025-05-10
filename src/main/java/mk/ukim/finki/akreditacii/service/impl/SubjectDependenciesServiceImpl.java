package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.SubjectDependencies;
import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidDependencyException;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;
import mk.ukim.finki.akreditacii.repository.StudyProgramRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDependenciesRepository;
import mk.ukim.finki.akreditacii.service.SubjectDependenciesService;
import mk.ukim.finki.akreditacii.service.specifications.SubjectDependencyProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectDependenciesServiceImpl implements SubjectDependenciesService {

    private final SubjectDependenciesRepository dependenciesRepository;
    private final List<SubjectDependencyProcessor> dependencyProcessors;
    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgramSubjectRepository studyProgramSubjectRepository;

    public SubjectDependenciesServiceImpl(SubjectDependenciesRepository dependenciesRepository,
                                          List<SubjectDependencyProcessor> dependencyProcessors,
                                          StudyProgramRepository studyProgramRepository,
                                          StudyProgramSubjectRepository studyProgramSubjectRepository) {
        this.dependenciesRepository = dependenciesRepository;
        this.dependencyProcessors = dependencyProcessors;
        this.studyProgramRepository = studyProgramRepository;
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
    }

    @Override
    public List<SubjectDependencies> findAll() {
        return dependenciesRepository.findAll();
    }

    @Override
    public SubjectDependencies findById(Long id) {
        return dependenciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject dependency with id " + id + " not found"));
    }

    @Override
    public List<SubjectDependencies> findBySubjectCode(String subjectCode) {
        return dependenciesRepository.findBySubjectCode(subjectCode);
    }

    @Override
    public List<SubjectDependencies> findBySubjectCodeAndStudyProgramCode(String subjectCode, String studyProgramCode) {
        return dependenciesRepository.findBySubjectCodeAndStudyProgramCode(subjectCode, studyProgramCode);
    }

    @Override
    public SubjectDependencies save(String subjectCode, String studyProgramCode, SubjectDependencyType dependencyType, String dependency) {
        SubjectDependencies newDependency = new SubjectDependencies();
        newDependency.setSubjectCode(subjectCode);
        newDependency.setStudyProgramCode(studyProgramCode);
        newDependency.setDependencyType(dependencyType);
        newDependency.setDependency(dependency);

        // Find the applicable processor for validation
        SubjectDependencyProcessor processor = findProcessor(dependencyType);

        // Validate the dependency string
        try {
            processor.validate(dependency);
        } catch (SubjectValidationException e) {
            throw new RuntimeException(e);
        }

        // If validation passes, save the dependency
        return dependenciesRepository.save(newDependency);
    }

    @Override
    public SubjectDependencies update(Long id, String subjectCode, String studyProgramCode, SubjectDependencyType dependencyType, String dependency) {
        // Find the existing dependency
        SubjectDependencies existingDependency = findById(id);

        // Update the fields
        existingDependency.setSubjectCode(subjectCode);
        existingDependency.setStudyProgramCode(studyProgramCode);
        existingDependency.setDependencyType(dependencyType);
        existingDependency.setDependency(dependency);

        // Find the applicable processor for validation
        SubjectDependencyProcessor processor = findProcessor(dependencyType);

        // Validate the dependency string
        try {
            processor.validate(dependency);
        } catch (SubjectValidationException e) {
            throw new RuntimeException(e);
        }

        // If validation passes, save the updated dependency
        return dependenciesRepository.save(existingDependency);
    }

    @Override
    public void deleteById(Long id) {
        dependenciesRepository.deleteById(id);
    }

    @Override
    public List<SubjectDependencies> getDependenciesForSubject(String subjectCode) {
        return dependenciesRepository.findBySubjectCode(subjectCode);
    }

    @Override
    public SubjectDependencies saveDependency(SubjectDependencies dependency) throws SubjectValidationException {
        // Find the applicable processor
        SubjectDependencyProcessor processor = findProcessor(dependency.getDependencyType());

        // Validate the dependency string
        processor.validate(dependency.getDependency());

        // If validation passes, save the dependency
        return dependenciesRepository.save(dependency);
    }

    @Override
    public void deleteDependency(Long dependencyId) {
        dependenciesRepository.deleteById(dependencyId);
    }

    @Override
    public boolean checkDependencySatisfaction(String subjectCode, String studyProgramCode,
                                               List<String> passedSubjects, List<String> enrolledSubjects) {
        // Get all dependencies for this subject in this study program
        List<SubjectDependencies> dependencies = dependenciesRepository.findBySubjectCodeAndStudyProgramCode(
                subjectCode, studyProgramCode);

        // Also get general dependencies (not study program specific)
        List<SubjectDependencies> generalDependencies = dependenciesRepository.findBySubjectCodeAndStudyProgramCodeIsNull(
                subjectCode);

        // Combine both lists
        dependencies.addAll(generalDependencies);

        // Check each dependency
        try {
            for (SubjectDependencies dependency : dependencies) {
                SubjectDependencyProcessor processor = findProcessor(dependency.getDependencyType());

                // Call isSatisfied for all types of dependencies using passedSubjects
                processor.isSatisfied(dependency.getDependency(), passedSubjects);
            }
            return true;
        } catch (InvalidDependencyException e) {
            return false;
        }
    }

    @Override
    public List<String> getStudyProgramsForSubject(String subjectCode) {
        // Since findBySubjectCode is not available in StudyProgramRepository,
        // we need to implement an alternative approach
        // For example, we could find all StudyProgramSubject entries for the given subject code
        // and extract the study program codes from there

        // You would need to add a repository method or query to find study programs by subject code
        // Example implementation:
        return studyProgramRepository.findAll().stream()
                .filter(sp -> hasSubject(sp, subjectCode))
                .map(StudyProgram::getCode)
                .collect(Collectors.toList());
    }

    // Helper method to check if a study program contains a specific subject
    private boolean hasSubject(StudyProgram program, String subjectCode) {
        // The ID format for StudyProgramSubject is [programCode]-[subjectCode]
        // So we can check if such an entry exists in the StudyProgramSubject repository
        String studyProgramSubjectId = program.getCode() + "-" + subjectCode;

        // Check if a StudyProgramSubject with this ID exists
        return studyProgramSubjectRepository.existsById(studyProgramSubjectId);
    }

    private SubjectDependencyProcessor findProcessor(SubjectDependencyType type) {
        return dependencyProcessors.stream()
                .filter(processor -> processor.applicableTo(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No processor found for type: " + type));
    }
}