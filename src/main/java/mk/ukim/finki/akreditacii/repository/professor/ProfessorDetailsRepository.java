package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorDetailsRepository extends JpaRepository<ProfessorDetails, String> {

    List<ProfessorDetails> findAll();

    void deleteById(String id);
}
