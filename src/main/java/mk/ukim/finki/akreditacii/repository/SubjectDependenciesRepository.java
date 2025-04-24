package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.SubjectDependencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectDependenciesRepository extends JpaRepository<SubjectDependencies, Long> {
    List<SubjectDependencies> findBySubjectCode(String subjectCode);

    List<SubjectDependencies> findBySubjectCodeAndStudyProgramCode(String subjectCode, String studyProgramCode);

    List<SubjectDependencies> findBySubjectCodeAndStudyProgramCodeIsNull(String subjectCode);
}