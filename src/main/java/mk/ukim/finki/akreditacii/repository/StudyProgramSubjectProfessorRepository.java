package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.dto.StudyProgramSubjectProfessorDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectAllocationStatsDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyProgramSubjectProfessorRepository extends JpaRepository<StudyProgramSubjectProfessor, String> {

    List<StudyProgramSubjectProfessor> findAllByStudyProgramSubjectSubjectId(String subjectId);

    List<StudyProgramSubjectProfessor> findAll(Specification<StudyProgramSubjectProfessor> spec);

    @Query("SELECT new mk.ukim.finki.akreditacii.model.subject.dto.StudyProgramSubjectProfessorDTO(" +
            "s.studyProgramSubject.subject.subject.id, " +
            "s.professor.id, " +
            "s.studyProgramSubject.studyProgram.code, " +
            "s.studyProgramSubject.mandatory) " +
            "FROM StudyProgramSubjectProfessor s")
    List<StudyProgramSubjectProfessorDTO> findAllCustomQuery();
}
