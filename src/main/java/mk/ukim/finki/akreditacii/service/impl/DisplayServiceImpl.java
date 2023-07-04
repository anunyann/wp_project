package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidStudyProgram;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.StudyProgramDetailsRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDetailsRepository;
import mk.ukim.finki.akreditacii.service.DisplayService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisplayServiceImpl implements DisplayService {

    private final SubjectDetailsRepository subjectDetailsRepository;

    private final StudyProgramDetailsRepository programDetailsRepository;

    private final StudyProgramSubjectRepository studyProgramSubjectRepository;
    private final StudyProgramSubjectProfessorRepository professorRepository;

    public DisplayServiceImpl(SubjectDetailsRepository repository,
                              StudyProgramDetailsRepository programDetailsRepository,
                              StudyProgramSubjectRepository studyProgramSubjectRepository,
                              StudyProgramSubjectProfessorRepository professorRepository) {
        this.subjectDetailsRepository = repository;
        this.programDetailsRepository = programDetailsRepository;
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public SubjectDetails getSubjectDetailsById(String subjectId) {
        return subjectDetailsRepository.findById(subjectId).orElseThrow(() -> new InvalidSubjectId(subjectId));
    }

    @Override
    public List<StudyProgramSubject> getSubjectPrograms(String subjectId) {
        return studyProgramSubjectRepository.findAllBySubjectId(subjectId);
    }

    @Override
    public List<StudyProgramSubject> getProgramSubjects(String programCode) {
        return studyProgramSubjectRepository.findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(programCode);
    }

    @Override
    public List<Professor> getSubjectProfessors(String subjectId) {
        return professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(StudyProgramSubjectProfessor::getProfessor)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<StudyProgramDetails> findAccreditationProgramsByCycle(String accreditation, StudyCycle cycle) {
        return programDetailsRepository.findAllByAccreditationYearAndStudyCycleOrderByDurationYearsDescOrderAsc(accreditation, cycle);
    }

    @Override
    public StudyProgramDetails getStudyProgramDetailsById(String program) {
        return this.programDetailsRepository.findById(program).orElseThrow(() -> new InvalidStudyProgram(program));
    }
}
