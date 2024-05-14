package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface StudyProgramSubjectRepository extends JpaRepository<StudyProgramSubject, String> {

    List<StudyProgramSubject> findAllBySubjectId(String subjectId);
    List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode);
}
