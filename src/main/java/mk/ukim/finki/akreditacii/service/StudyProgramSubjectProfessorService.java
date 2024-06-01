package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;

import java.util.List;

public interface StudyProgramSubjectProfessorService {


    List<Professor> professorsNotOnSubject(String subjectId);

    List<Professor> findProfessorsFromOtherProgramsForSubject(String subjectId, String currentProgramCode);
    void deleteProfessorForSubject(Professor professor, StudyProgramSubject studyProgramSubject);
    StudyProgramSubjectProfessor save(String newId,String studyProgramSubjectId, String professorId,Float newOrder);

    List<StudyProgramSubjectProfessor> professorsFromStudyProgramSubject(StudyProgramSubject studyProgramSubject);

    List<StudyProgramSubjectProfessor> saveAll(List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList);

    List<Professor> professorsOnAStudyProgram(String programCode);
}
