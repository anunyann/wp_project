package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, String> {


}
