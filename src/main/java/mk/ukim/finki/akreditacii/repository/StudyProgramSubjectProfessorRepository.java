package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.dto.StudyProgramSubjectProfessorDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectAllocationStatsDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    boolean existsByStudyProgramSubjectId(String subjectId);
    @Query("SELECT p FROM Professor p " +
            "WHERE p.id NOT IN (" +
            "   SELECT sp.professor.id FROM StudyProgramSubjectProfessor sp " +
            "   WHERE sp.studyProgramSubject.id = :subjectId" +
            ")")
    List<Professor> findProfessorsNotAssignedForSubject(String subjectId);



    @Query("SELECT DISTINCT s.professor " +
            "FROM StudyProgramSubjectProfessor s " +
            "JOIN s.studyProgramSubject ss " +
            "WHERE ss.subject.id = :subjectId " +
            "AND ss.studyProgram.code <> :currentProgramCode " +
            "AND ss.id LIKE CONCAT('%', :subjectId)")
    List<Professor> findProfessorsFromOtherProgramsForSubject(@Param("subjectId") String subjectId,
                                                              @Param("currentProgramCode") String currentProgramCode);

    List<StudyProgramSubjectProfessor> findAllByStudyProgramSubject(StudyProgramSubject studyProgramSubjectId);

    @Query("SELECT DISTINCT s.professor " +
            "FROM StudyProgramSubjectProfessor s " +
            "JOIN s.studyProgramSubject ss " +
            "WHERE ss.studyProgram.code = :programCode")
    List<Professor> findProfessorsByStudyProgramCode(String programCode);


    void deleteStudyProgramSubjectProfessorByProfessorAndStudyProgramSubject(Professor professor,StudyProgramSubject studyProgramSubject);
}
