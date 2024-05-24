package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAccreditationStats;
import org.springframework.data.domain.Page;

public interface ProfessorAccreditationStatsService {
    Page<ProfessorAccreditationStats> findAllWithPaginationAndFilters(Integer pageNum, Integer results, String accreditationYear, StudyCycle studyCycle);
}
