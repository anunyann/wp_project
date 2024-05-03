package mk.ukim.finki.akreditacii.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.*;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDetailsRepository;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        existingDetails.setQualityControl(updatedDetails.getQualityControl());
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


        return subjectDetailsRepository.findAllFiltered(nameSearch, filteredAccreditation, pageRequest);

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
    public List<String> getSubjectProfessorsCodes(String subjectId) {
        return professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(obj -> obj.getProfessor().getId())
                .distinct()
                .toList();
    }

    @Override
    public Integer getNumberOfProfessorsOnSubject(String subjectId) {
        return (int) professorRepository.findAllByStudyProgramSubjectSubjectId(subjectId).stream()
                .map(StudyProgramSubjectProfessor::getProfessor)
                .distinct()
                .count();
    }

    @Override
    public List<SubjectStatisticsDTO> findSubjectsInfo(String subjectCode, String professorCode, String studyProgramCode, String accreditationYear) {
        // implement this with specification search
        Specification<StudyProgramSubjectProfessor> spec = where(null);

        if (!StringUtils.isEmpty(subjectCode)) {
            spec = spec.and(filterEquals(StudyProgramSubjectProfessor.class, "studyProgramSubject.subject.subject.id", subjectCode));
        }

        if (!StringUtils.isEmpty(professorCode)) {
            spec = spec.and(filterEquals(StudyProgramSubjectProfessor.class, "professor.id", professorCode));
        }

        if (!StringUtils.isEmpty(studyProgramCode)) {
            spec = spec.and(filterEquals(StudyProgramSubjectProfessor.class, "studyProgramSubject.studyProgram.code", studyProgramCode));
        }

        if (!StringUtils.isEmpty(accreditationYear)) {
            spec = spec.and(filterEqualsV(StudyProgramSubjectProfessor.class, "studyProgramSubject.subject.accreditation.year", accreditationYear));
        }

        List<StudyProgramSubjectProfessor> studyProgramSubjectProfessors = this.professorRepository.findAll(spec);

        Map<String, List<StudyProgramSubjectProfessor>> subjects1 = studyProgramSubjectProfessors.stream().collect(Collectors.groupingBy(obj -> obj.getStudyProgramSubject().getSubject().getSubject().getId()));

        return createSubjectStatisticsDTO(subjects1);
    }

    private List<SubjectStatisticsDTO> createSubjectStatisticsDTO(Map<String, List<StudyProgramSubjectProfessor>> subjectsMap) {
        List<String> subjectCodes = subjectsMap.keySet().stream().toList();

        List<SubjectStatisticsDTO> subjectStatisticsDTOS = new ArrayList<>();
        for (String code : subjectCodes){
            List<StudyProgramSubjectProfessor> subjectsInfoList = subjectsMap.get(code);

            StudyProgramSubjectProfessor template = subjectsInfoList.get(0);
            // attributes
            String subjectName = template.getStudyProgramSubject().getSubject().getSubject().getId();
            List<Professor> professors = this.getSubjectProfessors(subjectName);
            List<String> professorCodes = professors.stream().map(Professor::getId).distinct().toList();
            Integer professorNumber = professorCodes.size();
            List<String> mandatoryStudyPrograms = new ArrayList<>();
            subjectsInfoList.forEach(obj -> {
                if (obj.getStudyProgramSubject().getMandatory())
                    mandatoryStudyPrograms.add(obj.getStudyProgramSubject().getStudyProgram().getCode());
            });
            Integer yearsActive = 0;
            Double numberOfFirstTimeStudents = 0.0;
            Double numberOfReEnrollmentStudents = 0.0;

            subjectStatisticsDTOS.add(new SubjectStatisticsDTO(subjectName, professorCodes, professorNumber, mandatoryStudyPrograms, yearsActive, numberOfFirstTimeStudents, numberOfReEnrollmentStudents));
        }

        return subjectStatisticsDTOS;
    }

    private SubjectAllocationStatsDTO getAllocationStats(String subjectCode){

//        Integer yearsActive = this.;
        Double numberOfFirstTimeStudents = 0.0;
        Double numberOfReEnrollmentStudents = 0.0;

        return null;
    }

    @Override
    public List<StudyProgram> getStudyProgramsWhereSubjectIsMandatory(String subjectId) {
        List<StudyProgramSubject> studyProgramSubjects = this.studyProgramSubjectRepository.findAllBySubjectId(subjectId).stream()
                .distinct()
                .collect(Collectors.toList());

        studyProgramSubjects.removeIf(sps -> sps.getMandatory().equals(false));

        return studyProgramSubjects.stream().map(StudyProgramSubject::getStudyProgram).toList();
    }

    @Override
    public List<StudyProgram> getStudyProgramsWhereSubjectIsNotMandatory(String subjectId) {
        List<StudyProgramSubject> studyProgramSubjects = this.studyProgramSubjectRepository.findAllBySubjectId(subjectId).stream()
                .distinct()
                .collect(Collectors.toList());

        studyProgramSubjects.removeIf(sps -> sps.getMandatory().equals(true));

        return studyProgramSubjects.stream().map(StudyProgramSubject::getStudyProgram).toList();
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
