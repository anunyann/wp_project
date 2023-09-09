package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SubjectService {

    void updateSubject(SubjectDetails sd);

    Optional<SubjectDetails> findSubjectById(String id);

    List<SubjectDetails> findAll();

    SubjectDetails getSubjectDetailsById(String subjectId);

    List<StudyProgramSubject> getSubjectPrograms(String subjectId);


    List<Professor> getSubjectProfessors(String subjectId);

}