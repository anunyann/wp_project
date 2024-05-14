package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, String> {
}
