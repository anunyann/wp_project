package mk.ukim.finki.akreditacii.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.exceptions.StudyProgramDetailsCannotBeDeletedException;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.repository.AccreditationRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramDetailsRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramRepository;
import mk.ukim.finki.akreditacii.service.StudyProgramDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyProgramDetailsServiceImpl implements StudyProgramDetailsService {
    private final StudyProgramDetailsRepository studyProgramDetailsRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final AccreditationRepository accreditationRepository;

    public StudyProgramDetailsServiceImpl(StudyProgramDetailsRepository studyProgramDetailsRepository, StudyProgramRepository studyProgramRepository, AccreditationRepository accreditationRepository) {
        this.studyProgramDetailsRepository = studyProgramDetailsRepository;
        this.studyProgramRepository = studyProgramRepository;
        this.accreditationRepository = accreditationRepository;
    }

    @Override
    public List<StudyProgramDetails> findAll() {
        return studyProgramDetailsRepository.findAll();
    }

    @Override
    public Page<StudyProgramDetails> findAllWithPagination(Integer pageNum, Integer results) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);
        return studyProgramDetailsRepository.findAll(pageRequest);
    }

    @Override
    public Page<StudyProgramDetails> findAllWithPaginationFiltered(Integer pageNum, Integer results, String nameSearch, String filteredAccreditation, String filteredStudyCycle, String filteredDurationInYears, String filteredOnEnglish) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        return studyProgramDetailsRepository.findAllFiltered(nameSearch, filteredAccreditation,
                !filteredStudyCycle.equals("") ? StudyCycle.valueOf(filteredStudyCycle) : null,
                !filteredDurationInYears.equals("") ? Short.valueOf(filteredDurationInYears) : null,
                filteredOnEnglish != null,
                pageRequest);
    }

    @Override
    public Optional<StudyProgramDetails> findById(String code) {
        return studyProgramDetailsRepository.findById(code);
    }

    @Override
    @Transactional
    public Optional<StudyProgramDetails> save(String code, String name, String nameEn, Float order, Short durationYears,
                                              Short durationSemesters, String generalInformation, String graduationTitle,
                                              String graduationTitleEn, String subjectRestrictions, Boolean onEnglish,
                                              StudyCycle studyCycle, Accreditation accreditation, Boolean bilingual,
                                              Professor coordinator) {

        StudyProgram newStudyProgram = studyProgramRepository.save(new StudyProgram(code, name));

        return Optional.of(studyProgramDetailsRepository.save(
                        new StudyProgramDetails(
                                code,
                                newStudyProgram,
                                nameEn,
                                order,
                                durationYears,
                                durationSemesters,
                                generalInformation,
                                graduationTitle,
                                graduationTitleEn,
                                subjectRestrictions,
                                onEnglish,
                                studyCycle,
                                accreditation,
                                bilingual,
                                null,
                                coordinator
                        )
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(String code) {
        if (this.findById(code).isPresent()) {
            StudyProgramDetails studyProgramDetails = this.findById(code).get();
            StudyProgram studyProgram = studyProgramDetails.getStudyProgram();

            try {
                studyProgramDetailsRepository.deleteById(studyProgramDetails.getId());
                if (studyProgram != null) {
                    studyProgramRepository.delete(studyProgram);
                }
            } catch (Exception e) {
                throw new StudyProgramDetailsCannotBeDeletedException(code);
            }
        }
    }

}
