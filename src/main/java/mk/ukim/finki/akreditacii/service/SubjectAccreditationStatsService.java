package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.subject.SubjectAccreditationStats;
import org.springframework.data.domain.Page;

public interface SubjectAccreditationStatsService {
    Page<SubjectAccreditationStats> findAllWithPaginationAndFilters(Integer pageNumber, Integer result, String subjectCode, String professorCode, String studyProgramCode, String selectedAccreditationYear);
}
