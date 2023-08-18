package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.EducationDegree;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProfessorDetailsRepository extends JpaRepository<ProfessorDetails, String> {

List<ProfessorDetails> findAll();
    ProfessorDetails findByProfessorId(String id);
    ProfessorDetails findByProfessor(Professor professor);
    void deleteById(String id);
}
