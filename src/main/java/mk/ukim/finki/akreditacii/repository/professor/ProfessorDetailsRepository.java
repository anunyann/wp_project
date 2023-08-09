package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorDetailsRepository extends JpaRepository<ProfessorDetails, String> {


    ProfessorDetails findByProfessorId(String id);

}
