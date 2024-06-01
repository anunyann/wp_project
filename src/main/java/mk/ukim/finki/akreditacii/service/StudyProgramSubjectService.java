package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;

import java.util.List;

public interface StudyProgramSubjectService {
    List<StudyProgramSubject> findByStudyProgram(StudyProgramDetails studyProgramDetails);
    StudyProgramSubject findById(String subjectId);
    StudyProgramSubject save(StudyProgramSubject studyProgramSubject);
    StudyProgramSubject edit(String subjectId, String name, Boolean mandatory, short semester);

    List<StudyProgramSubject> findAllByStudyProgram(String studyProgramId);
    void remove(String subjectId);

    void add(String id, String subjectId, boolean mandatory, short semester, float order);

    boolean hasAssociatedProfessors(String subjectId);
    void add(String id, String subjectId, boolean mandatory, short semester);
    List<StudyProgramSubject> findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(String programCode);

}
