package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidStudyProgram;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidStudyProgramSubjectId;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDetailsRepository;
import mk.ukim.finki.akreditacii.service.StudyProgramService;
import mk.ukim.finki.akreditacii.service.StudyProgramSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyProgramSubjectServiceImpl implements StudyProgramSubjectService {
    private final StudyProgramSubjectRepository studyProgramSubjectRepository;
    private final SubjectDetailsRepository subjectDetailsRepository;
    private final StudyProgramService studyProgramService;
    private final StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository;


    public StudyProgramSubjectServiceImpl(StudyProgramSubjectRepository studyProgramSubjectRepository, SubjectDetailsRepository subjectDetailsRepository, StudyProgramService studyProgramService, StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository) {
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
        this.subjectDetailsRepository = subjectDetailsRepository;
        this.studyProgramService = studyProgramService;
        this.studyProgramSubjectProfessorRepository = studyProgramSubjectProfessorRepository;
    }

    @Override
    public List<StudyProgramSubject> findByStudyProgram(StudyProgramDetails studyProgramDetails) {
        return studyProgramSubjectRepository.findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(studyProgramDetails.getStudyProgram().getCode());
    }

    @Override
    public StudyProgramSubject findById(String subjectId) {
        return studyProgramSubjectRepository.findById(subjectId).orElseThrow(() -> new InvalidStudyProgramSubjectId(subjectId));
    }

    @Override
    public StudyProgramSubject save(StudyProgramSubject studyProgramSubject) {
        return studyProgramSubjectRepository.save(studyProgramSubject);
    }

    @Override
    public StudyProgramSubject edit(String studyProgramSubjectId, String name, Boolean mandatory, short semester,
                                    String subjectGroup, Float order, String dependenciesOverride) {
        StudyProgramSubject subject = findById(studyProgramSubjectId);


        subject.getSubject().getSubject().setName(name);
        subject.setMandatory(mandatory);
        subject.setSemester(semester);
        subject.setSubjectGroup(subjectGroup);
        subject.setOrder(order);
        subject.setDependenciesOverride(dependenciesOverride);

        studyProgramSubjectRepository.save(subject);
        return subject;
    }

    @Override
    public void remove(String subjectId) {
        studyProgramSubjectRepository.deleteById(subjectId);
    }

    @Override
    public void add(String id, String subjectId, boolean mandatory, short semester, float order) {
        StudyProgramSubject studyProgramSubject = new StudyProgramSubject();

        String studyProgramSubjectId = id + "-" + subjectId;
        studyProgramSubject.setId(studyProgramSubjectId);

        SubjectDetails subjectDetails = subjectDetailsRepository.findById(subjectId).orElseThrow(() -> new InvalidSubjectId(subjectId));

        studyProgramSubject.setSubject(subjectDetails);
        studyProgramSubject.setStudyProgram(studyProgramService.findById(id).orElseThrow(() -> new InvalidStudyProgram(id)));
        studyProgramSubject.setMandatory(mandatory);
        studyProgramSubject.setSemester(semester);
        studyProgramSubject.setDependenciesOverride(null);
        studyProgramSubject.setOrder(order);

        String prefix = subjectId.substring(0, 6);
        studyProgramSubject.setSubjectGroup(prefix);

        studyProgramSubjectRepository.save(studyProgramSubject);
    }

    @Override
    public List<StudyProgramSubject> findAllByStudyProgram(String studyProgramId) {
        return studyProgramSubjectRepository.findAllByStudyProgramCode(studyProgramId);
    }

    @Override
    public void add(String id, String subjectId, boolean mandatory, short semester) {
        StudyProgramSubject studyProgramSubject = new StudyProgramSubject();
        studyProgramSubject.setId(subjectId);

        String studyProgramSubjectId = id + "-" + subjectId;
        studyProgramSubject.setId(studyProgramSubjectId);

        SubjectDetails subjectDetails = subjectDetailsRepository.findById(subjectId).orElseThrow(() -> new InvalidSubjectId(subjectId));

        studyProgramSubject.setSubject(subjectDetails);
        studyProgramSubject.setStudyProgram(studyProgramService.findById(id).orElse(null));
        studyProgramSubject.setMandatory(mandatory);
        studyProgramSubject.setSemester(semester);
        studyProgramSubject.setDependenciesOverride(null);
        studyProgramSubject.setOrder(100f);

        String prefix = subjectId.substring(0, 6);
        studyProgramSubject.setSubjectGroup(prefix);

        studyProgramSubjectRepository.save(studyProgramSubject);
    }

    @Override
    public boolean hasAssociatedProfessors(String subjectId) {
        return studyProgramSubjectProfessorRepository.existsByStudyProgramSubjectId(subjectId);
    }


    public List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode) {
        return studyProgramSubjectRepository.findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(programCode);
    }


}