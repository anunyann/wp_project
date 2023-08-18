package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorEducationRepository extends JpaRepository<ProfessorEducation, String> {

    List<ProfessorEducation> findAllByProfessor(Professor professor);
    void deleteByEducationId (String id);
    ProfessorEducation findByEducationId(String id);

    void deleteAllByProfessor(Professor professor);
}
