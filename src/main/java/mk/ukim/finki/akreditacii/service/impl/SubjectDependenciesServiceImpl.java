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

        SubjectDependencyProcessor processor = findProcessor(dependencyType);

        try {
            processor.validate(dependency);
        } catch (SubjectValidationException e) {
            throw new RuntimeException(e);
        }

        return dependenciesRepository.save(newDependency);
    }

    @Override
    public SubjectDependencies update(Long id, String subjectCode, String studyProgramCode, SubjectDependencyType dependencyType, String dependency) {
        SubjectDependencies existingDependency = findById(id);

        existingDependency.setSubjectCode(subjectCode);
        existingDependency.setStudyProgramCode(studyProgramCode);
        existingDependency.setDependencyType(dependencyType);
        existingDependency.setDependency(dependency);

        SubjectDependencyProcessor processor = findProcessor(dependencyType);

        try {
            processor.validate(dependency);
        } catch (SubjectValidationException e) {
            throw new RuntimeException(e);
        }

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
        SubjectDependencyProcessor processor = findProcessor(dependency.getDependencyType());

        processor.validate(dependency.getDependency());

        return dependenciesRepository.save(dependency);
    }

    @Override
    public void deleteDependency(Long dependencyId) {
        dependenciesRepository.deleteById(dependencyId);
    }

    @Override
    public boolean checkDependencySatisfaction(String subjectCode, String studyProgramCode,
                                               List<String> passedSubjects, List<String> enrolledSubjects) {
        List<SubjectDependencies> dependencies = dependenciesRepository.findBySubjectCodeAndStudyProgramCode(
                subjectCode, studyProgramCode);

        List<SubjectDependencies> generalDependencies = dependenciesRepository.findBySubjectCodeAndStudyProgramCodeIsNull(
                subjectCode);

        dependencies.addAll(generalDependencies);

        try {
            for (SubjectDependencies dependency : dependencies) {
                SubjectDependencyProcessor processor = findProcessor(dependency.getDependencyType());

                processor.isSatisfied(dependency.getDependency(), passedSubjects);
            }
            return true;
        } catch (InvalidDependencyException e) {
            return false;
        }
    }
    @Override
    public List<String> getStudyProgramsForSubject(String subjectCode) {
        return studyProgramRepository.findAll().stream()
                .filter(sp -> hasSubject(sp, subjectCode))
                .map(StudyProgram::getCode)
                .collect(Collectors.toList());
    }
    private boolean hasSubject(StudyProgram program, String subjectCode) {
        String studyProgramSubjectId = program.getCode() + "-" + subjectCode;
        return studyProgramSubjectRepository.existsById(studyProgramSubjectId);
    }
    private SubjectDependencyProcessor findProcessor(SubjectDependencyType type) {
        return dependencyProcessors.stream()
                .filter(processor -> processor.applicableTo(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No processor found for type: " + type));
    }
}