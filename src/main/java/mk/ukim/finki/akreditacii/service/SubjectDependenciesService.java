package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.SubjectDependencies;
import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.model.exceptions.SubjectValidationException;

import java.util.List;

public interface SubjectDependenciesService {

    List<SubjectDependencies> findAll();

    SubjectDependencies findById(Long id);

    List<SubjectDependencies> findBySubjectCode(String subjectCode);

    List<SubjectDependencies> findBySubjectCodeAndStudyProgramCode(String subjectCode, String studyProgramCode);

    SubjectDependencies save(String subjectCode, String studyProgramCode, SubjectDependencyType dependencyType, String dependency);

    SubjectDependencies update(Long id, String subjectCode, String studyProgramCode, SubjectDependencyType dependencyType, String dependency);

    void deleteById(Long id);

    List<SubjectDependencies> getDependenciesForSubject(String subjectCode);

    SubjectDependencies saveDependency(SubjectDependencies dependency) throws SubjectValidationException;

    void deleteDependency(Long dependencyId);

    boolean checkDependencySatisfaction(String subjectCode, String studyProgramCode,
                                        List<String> passedSubjects, List<String> enrolledSubjects);

    List<String> getStudyProgramsForSubject(String subjectCode);
}