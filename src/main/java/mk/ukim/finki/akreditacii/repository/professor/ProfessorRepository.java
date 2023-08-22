package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

}
