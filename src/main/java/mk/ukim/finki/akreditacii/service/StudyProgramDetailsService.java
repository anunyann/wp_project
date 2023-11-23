package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudyProgramDetailsService {
    List<StudyProgramDetails> findAll();

    Page<StudyProgramDetails> findAllWithPagination(Integer pageNum, Integer pageSize);

    Page<StudyProgramDetails> findAllWithPaginationFiltered(Integer pageNum, Integer results,
                                                            String nameSearch,
                                                            String filteredAccreditation,
                                                            String filteredStudyCycle,
                                                            String filteredDurationInYears,
                                                            String filteredOnEnglish);

    Optional<StudyProgramDetails> findById(String code);

    Optional<StudyProgramDetails> save(String code, String name, String nameEn, Float order,
                                       Short durationYears, Short durationSemesters,
                                       String generalInformation, String graduationTitle,
                                       String graduationTitleEn, String subjectRestrictions,
                                       Boolean onEnglish, StudyCycle studyCycle,
                                       Accreditation accreditation, Boolean bilingual,
                                       Professor coordinator);

    void deleteById(String code);
}
