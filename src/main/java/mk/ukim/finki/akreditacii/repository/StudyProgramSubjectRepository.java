package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyProgramSubjectRepository extends JpaRepository<StudyProgramSubject, String> {

    List<StudyProgramSubject> findAllBySubjectId(String subjectId);

   Optional<StudyProgramSubject> findById(String id);

    List<StudyProgramSubject> findAllByStudyProgramCode(String studyProgramId);
    List<StudyProgramSubject> findAll();
    List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode);
    StudyProgramSubject findBySubjectAndAndStudyProgram(Subject subject, StudyProgram studyProgram);
}
