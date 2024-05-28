package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectStatisticsDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SubjectDetailsService {

    void updateSubject(SubjectDetails sd);

    Optional<SubjectDetails> findSubjectById(String id);

    Page<SubjectDetails> findAllWithPagination(Integer pageNum, Integer pageSize);

    Page<SubjectDetails> findAllWithPaginationFiltered(Integer pageNum, Integer results,
                                                       String nameSearch,
                                                       String filteredAccreditation);

    List<SubjectDetails> findAll();

    SubjectDetails getSubjectDetailsById(String subjectId);

    List<SubjectNameAndCodeDTO> findAllSubjectNameAndCode();

    List<StudyProgramSubject> getSubjectPrograms(String subjectId);

    List<Professor> getSubjectProfessors(String subjectId);

    List<String> getSubjectProfessorsCodes(String subjectId);

    Integer getNumberOfProfessorsOnSubject(String subjectId);

    List<SubjectStatisticsDTO> findSubjectsInfo(String subjectCode, String professorCode, String studyProgramCode, String accreditationYear);

    List<StudyProgram> getStudyProgramsWhereSubjectIsMandatory(String subjectId);

    List<StudyProgram> getStudyProgramsWhereSubjectIsNotMandatory(String subjectId);

    Accreditation getActiveAccreditationYear();
}