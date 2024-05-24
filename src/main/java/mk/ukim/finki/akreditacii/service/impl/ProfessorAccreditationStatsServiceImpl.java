package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAccreditationStats;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorAccreditationStatsRepository;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import mk.ukim.finki.akreditacii.service.ProfessorAccreditationStatsService;
import mk.ukim.finki.akreditacii.service.specifications.FieldFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ProfessorAccreditationStatsServiceImpl implements ProfessorAccreditationStatsService {

    private final ProfessorAccreditationStatsRepository professorAccreditationStatsRepository;
    private final AccreditationService accreditationService;

    public ProfessorAccreditationStatsServiceImpl(ProfessorAccreditationStatsRepository professorAccreditationStatsRepository, AccreditationService accreditationService) {
        this.professorAccreditationStatsRepository = professorAccreditationStatsRepository;
        this.accreditationService = accreditationService;
    }

    @Override
    public Page<ProfessorAccreditationStats> findAllWithPaginationAndFilters(Integer pageNum, Integer results, String accreditationYear, StudyCycle studyCycle) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        if (accreditationYear == null || accreditationYear.isEmpty()) {
            accreditationYear = accreditationService.findActiveAccreditation().getYear();
        }

        Specification<ProfessorAccreditationStats> spec = Specification.where(
                FieldFilterSpecification.filterEquals(ProfessorAccreditationStats.class, "accreditationYear", accreditationYear)
                        .and(FieldFilterSpecification.filterEquals(ProfessorAccreditationStats.class, "cycle", studyCycle))
        );

        return professorAccreditationStatsRepository.findAll(spec, pageRequest);
    }
}
