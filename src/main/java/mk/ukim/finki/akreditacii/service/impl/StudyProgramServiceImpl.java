package mk.ukim.finki.akreditacii.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.repository.AccreditationRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramDetailsRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramRepository;
import mk.ukim.finki.akreditacii.service.StudyProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyProgramServiceImpl implements StudyProgramService {
    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgramDetailsRepository studyProgramDetailsRepository;
    private final AccreditationRepository accreditationRepository;

    public StudyProgramServiceImpl(StudyProgramRepository studyProgramRepository, StudyProgramDetailsRepository studyProgramDetailsRepository, AccreditationRepository accreditationRepository) {
        this.studyProgramRepository = studyProgramRepository;
        this.studyProgramDetailsRepository = studyProgramDetailsRepository;
        this.accreditationRepository = accreditationRepository;
    }

    @Override
    public List<StudyProgram> findAll() {
        return studyProgramRepository.findAll();
    }

    @Override
    public Page<StudyProgram> findAllWithPagination(Pageable pageable) {
        return studyProgramRepository.findAll(pageable);
    }

    @Override
    public Optional<StudyProgram> findById(String code) {
        return studyProgramRepository.findById(code);
    }

    @Override
    public void deleteById(String code) {
        studyProgramRepository.deleteById(code);
    }
}
