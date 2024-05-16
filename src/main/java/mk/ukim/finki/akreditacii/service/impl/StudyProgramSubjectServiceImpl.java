package mk.ukim.finki.akreditacii.service.impl;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
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


    public StudyProgramSubjectServiceImpl(StudyProgramSubjectRepository studyProgramSubjectRepository, SubjectDetailsRepository subjectDetailsRepository, StudyProgramService studyProgramService) {
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
        this.subjectDetailsRepository = subjectDetailsRepository;
        this.studyProgramService = studyProgramService;

    }



    @Override
    public StudyProgramSubject findById(String id) {
        return studyProgramSubjectRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public StudyProgramSubject save(StudyProgramSubject studyProgramSubject) {
        return studyProgramSubjectRepository.save(studyProgramSubject);
    }


    @Override
    public StudyProgramSubject edit(String studyProgramSubjectId, String name, Boolean mandatory, short semester) {
        StudyProgramSubject subject = studyProgramSubjectRepository.findById(studyProgramSubjectId).orElseThrow(RuntimeException::new);

        subject.getSubject().getSubject().setName(name);
        subject.setMandatory(mandatory);
        subject.setSemester(semester);

        studyProgramSubjectRepository.save(subject);
        return subject;
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

        SubjectDetails subjectDetails = subjectDetailsRepository.findById(subjectId).orElseThrow(RuntimeException::new);

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
    public List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode) {
        return studyProgramSubjectRepository.findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(programCode);
    }


}