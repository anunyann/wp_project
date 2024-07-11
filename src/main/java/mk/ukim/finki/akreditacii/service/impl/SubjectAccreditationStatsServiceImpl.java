package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.subject.SubjectAccreditationStats;
import mk.ukim.finki.akreditacii.repository.SubjectAccreditationStatsRepository;
import mk.ukim.finki.akreditacii.service.SubjectAccreditationStatsService;
import mk.ukim.finki.akreditacii.service.specifications.FieldFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SubjectAccreditationStatsServiceImpl implements SubjectAccreditationStatsService {

    private final SubjectAccreditationStatsRepository subjectAccreditationStatsRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubjectAccreditationStatsServiceImpl.class);

    public SubjectAccreditationStatsServiceImpl(SubjectAccreditationStatsRepository subjectAccreditationStatsRepository) {
        this.subjectAccreditationStatsRepository = subjectAccreditationStatsRepository;
    }

    @Override
    public Page<SubjectAccreditationStats> findAllWithPaginationAndFilters(Integer pageNumber, Integer result, String subjectCode, String professorCode, String studyProgramCode, String selectedAccreditationYear) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, result, Sort.by(Sort.Direction.DESC, "subjectName"));

        if (subjectCode == null) {
            subjectCode = "";
        }

        if (professorCode == null) {
            professorCode = "";
        }

        Specification<SubjectAccreditationStats> spec = Specification.where(
                FieldFilterSpecification.filterEquals(SubjectAccreditationStats.class, "id", subjectCode))
                        .and(FieldFilterSpecification.filterContainsText(SubjectAccreditationStats.class, "professors", professorCode))
                        .and(FieldFilterSpecification.filterContainsText(SubjectAccreditationStats.class, "allStudyPrograms", studyProgramCode))
                        .and(FieldFilterSpecification.filterEquals(SubjectAccreditationStats.class, "accreditationYear", selectedAccreditationYear));


        Page<SubjectAccreditationStats> page = subjectAccreditationStatsRepository.findAll(spec, pageRequest);

        // Log the query to debug
        logger.debug("Query executed: " + pageRequest.toString());

        return page;
    }

}
