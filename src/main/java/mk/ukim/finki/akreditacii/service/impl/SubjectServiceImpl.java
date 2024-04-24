package mk.ukim.finki.akreditacii.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.akreditacii.model.dto.SubjectInfoDto;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDetailsRepository;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mk.ukim.finki.akreditacii.service.specifications.FieldFilterSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectDetailsRepository subjectDetailsRepository;
    private final StudyProgramSubjectRepository studyProgramSubjectRepository;
    private final StudyProgramSubjectProfessorRepository professorRepository;

    public SubjectServiceImpl(SubjectDetailsRepository subjectDetailsRepository, StudyProgramSubjectRepository studyProgramSubjectRepository, StudyProgramSubjectProfessorRepository professorRepository) {
        this.subjectDetailsRepository = subjectDetailsRepository;
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
        this.professorRepository = professorRepository;
    }


    @Override
    public void updateSubject(SubjectDetails updatedDetails) {

        SubjectDetails existingDetails = subjectDetailsRepository.findById(updatedDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("SubjectDetails not found"));

        existingDetails.getSubject().setName(updatedDetails.getSubject().getName());
        existingDetails.setId(updatedDetails.getId());
        existingDetails.setNameEn(updatedDetails.getNameEn());
        existingDetails.getSubject().setId(updatedDetails.getSubject().getId());
        existingDetails.setCycle(updatedDetails.getCycle());
        existingDetails.setDefaultSemester(updatedDetails.getDefaultSemester());
        existingDetails.getSubject().setSemester(updatedDetails.getSubject().getSemester());
        existingDetails.setCredits(updatedDetails.getCredits());
        existingDetails.setObligationDuration(updatedDetails.getObligationDuration());
        existingDetails.getObligationDuration().setExerciseHours(updatedDetails.getObligationDuration().getExerciseHours());
        existingDetails.getObligationDuration().setSelfLearningHours(updatedDetails.getObligationDuration().getSelfLearningHours());
        existingDetails.getObligationDuration().setProjectHours(updatedDetails.getObligationDuration().getProjectHours());
        existingDetails.getObligationDuration().setHomeworkHours(updatedDetails.getObligationDuration().getHomeworkHours());
        existingDetails.getGrading().setTestsPoints(updatedDetails.getGrading().getTestsPoints());
        existingDetails.getGrading().setProjectPoints(updatedDetails.getGrading().getProjectPoints());
        existingDetails.getGrading().setActivityPoints(updatedDetails.getGrading().getActivityPoints());
        existingDetails.getGrading().setExamPoints(updatedDetails.getGrading().getExamPoints());
        existingDetails.getGrading().setSignatureCondition(updatedDetails.getGrading().getSignatureCondition());
        existingDetails.setLanguage(updatedDetails.getLanguage());
        existingDetails.setQualityControl(updatedDetails.getQualityControl() );
        existingDetails.setGoalsDescription(updatedDetails.getGoalsDescription());
        existingDetails.setContent(updatedDetails.getContent());
        existingDetails.setLearningMethods(updatedDetails.getLearningMethods());

        subjectDetailsRepository.save(existingDetails);
    }

    @Override
    public Optional<SubjectDetails> findSubjectById(String id) {
        return subjectDetailsRepository.findById(id);
    }

    @Override
    public Page<SubjectDetails> findAllWithPagination(Integer pageNum, Integer results) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);
        return subjectDetailsRepository.findAll(pageRequest);
    }

    @Override
    public Page<SubjectDetails> findAllWithPaginationFiltered(Integer pageNum, Integer results,
                                                              String nameSearch,
                                                              String filteredAccreditation) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);


        return subjectDetailsRepository.findAllFiltered(nameSearch,filteredAccreditation,pageRequest);

    }

    @Override
    public List<SubjectDetails> findAll() {
        return subjectDetailsRepository.findAll();
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
    public List<Professor> getSubjectProfessors(String subjectId) {
        return professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(StudyProgramSubjectProfessor::getProfessor)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String getSubjectProfessorsSeparatedWithComma(String subjectId) {
        // Get all professors on given subject
        List<Professor> professors = professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(StudyProgramSubjectProfessor::getProfessor)
                .distinct()
                .toList();

        if (professors.isEmpty())
            return "";

        StringBuilder string = new StringBuilder();
        professors.forEach(p -> string.append(p.getId()).append(", "));

        string.deleteCharAt(string.length() - 1);
        string.deleteCharAt(string.length() - 1);
        string.append(".");

        return string.toString();
    }

    @Override
    public Integer getNumberOfProfessorsOnSubject(String subjectId) {
        return (int) professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(StudyProgramSubjectProfessor::getProfessor)
                .distinct()
                .count();
    }

    @Override
    public List<SubjectInfoDto> findSubjectsInfo(String name, String professorId, String studyProgramCode, String accreditationYear) {
        // todo: implement this with specification search
//        Specification<StudyProgramSubjectProfessor> spec = where(filterContainsText(StudyProgramSubjectProfessor.class, "name", name))
//                .and(filterEquals(StudyProgramSubjectProfessor.class, "mainSubject.id", mainSubject))
//                .and(filterEqualsV(StudyProgramSubjectProfessor.class, "semesterType", semesterType))
//                .and(greaterThan(StudyProgramSubjectProfessor.class, "lastUpdateTime", modifiedAfter));
        return null;
    }

    @Override
    public List<StudyProgram> getStudyProgramsWhereSubjectIsMandatory(String subjectId) {
        List<StudyProgramSubject> studyProgramSubjects = this.studyProgramSubjectRepository.findAllBySubjectId(subjectId).stream()
                .distinct()
                .collect(Collectors.toList());

        studyProgramSubjects.removeIf(sps -> sps.getMandatory().equals(false));

        List<StudyProgram> programs = studyProgramSubjects.stream().map(StudyProgramSubject::getStudyProgram).toList();

        return studyProgramSubjects.stream().map(StudyProgramSubject::getStudyProgram).toList();
    }

    @Override
    public String getStudyProgramsWhereSubjectIsMandatorySeparatedWithComma(String subjectId) {
        List<StudyProgramSubject> studyProgramSubjects = this.studyProgramSubjectRepository.findAllBySubjectId(subjectId).stream()
                .distinct()
                .collect(Collectors.toList());

        studyProgramSubjects.removeIf(sps -> sps.getMandatory().equals(false));

        List<StudyProgram> programs = studyProgramSubjects.stream().map(StudyProgramSubject::getStudyProgram).toList();

        if (programs.isEmpty())
            return "";

        StringBuilder string = new StringBuilder();
        programs.forEach(p -> string.append(p.getCode()).append(", "));

        string.deleteCharAt(string.length() - 1);
        string.deleteCharAt(string.length() - 1);
        string.append(".");

        return string.toString();
    }

    @Override
    public Integer numberOfActiveYears(String subjectId) {
        SubjectDetails subjectDetails = subjectDetailsRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("SubjectDetails not found"));

        Integer startYear = Integer.valueOf(subjectDetails.getAccreditation().getYear());
        Integer currentYear = LocalDateTime.now().getYear();

        return currentYear - startYear;
    }

    @Override
    public Double averageNumberOfStudents(String subjectId) {
        return null;
    }
}
