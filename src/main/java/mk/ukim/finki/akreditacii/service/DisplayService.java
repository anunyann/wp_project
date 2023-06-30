package mk.ukim.finki.akreditacii.service;


import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;

import java.util.List;

public interface DisplayService {
    SubjectDetails getSubjectDetailsById(String subjectId);

    List<StudyProgramSubject> getSubjectPrograms(String subjectId);

    List<StudyProgramSubject> getProgramSubjects(String programCode);

    List<Professor> getSubjectProfessors(String subjectId);

    List<StudyProgramDetails> findAccreditationProgramsByCycle(String accreditation, StudyCycle cycle);
}
