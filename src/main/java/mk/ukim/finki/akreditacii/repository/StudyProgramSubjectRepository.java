package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StudyProgramSubjectRepository extends JpaRepository<StudyProgramSubject, String> {

    List<StudyProgramSubject> findAllBySubjectId(String subjectId);

    List<StudyProgramSubject> findAllByStudyProgramCode(String studyProgramId);

    List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode);

}
