package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, String> {
}
