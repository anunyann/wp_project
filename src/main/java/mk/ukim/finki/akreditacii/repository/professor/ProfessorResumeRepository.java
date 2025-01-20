package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.ProfessorResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorResumeRepository extends JpaRepository<ProfessorResume, String> {
    ProfessorResume findByProfessorId(String id);
}
