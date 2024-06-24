package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.professor.ProfessorAccreditationStats;
import mk.ukim.finki.akreditacii.model.subject.SubjectAccreditationStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SubjectAccreditationStatsRepository extends JpaSpecificationRepository<SubjectAccreditationStats, String> {
    Page<SubjectAccreditationStats> findAll(Specification specification, Pageable pageable);
}
