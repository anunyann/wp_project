package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyProgramSubjectProfessorRepository extends JpaRepository<StudyProgramSubjectProfessor, String> {

    List<StudyProgramSubjectProfessor> findAllByStudyProgramSubjectSubjectId(String subjectId);

    List<StudyProgramSubjectProfessor> findAll(Specification<StudyProgramSubjectProfessor> spec);
}
