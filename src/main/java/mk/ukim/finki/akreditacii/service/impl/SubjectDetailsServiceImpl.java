package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.model.subject.dto.StudyProgramSubjectProfessorDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectAllocationStatsDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectStatisticsDTO;
import mk.ukim.finki.akreditacii.repository.*;
import mk.ukim.finki.akreditacii.service.SubjectDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static mk.ukim.finki.akreditacii.service.specifications.FieldFilterSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class SubjectDetailsServiceImpl implements SubjectDetailsService {

    private final SubjectDetailsRepository subjectDetailsRepository;
    private final StudyProgramSubjectRepository studyProgramSubjectRepository;
    private final StudyProgramSubjectProfessorRepository professorRepository;
    private final AccreditationRepository accreditationRepository;
    private final SubjectAccreditationStatsRepository subjectAccreditationStatsRepository;

    public SubjectDetailsServiceImpl(SubjectDetailsRepository subjectDetailsRepository, StudyProgramSubjectRepository studyProgramSubjectRepository, StudyProgramSubjectProfessorRepository professorRepository, AccreditationRepository accreditationRepository, SubjectAccreditationStatsRepository subjectAccreditationStatsRepository) {
        this.subjectDetailsRepository = subjectDetailsRepository;
        this.studyProgramSubjectRepository = studyProgramSubjectRepository;
        this.professorRepository = professorRepository;
        this.accreditationRepository = accreditationRepository;
        this.subjectAccreditationStatsRepository = subjectAccreditationStatsRepository;
    }


    @Override
    public void updateSubject(SubjectDetails updatedDetails) {

        SubjectDetails existingDetails = subjectDetailsRepository.findById(updatedDetails.getId())
                .orElse(new SubjectDetails());

        existingDetails.setName(updatedDetails.getName());
        existingDetails.setId(updatedDetails.getId());
        existingDetails.setNameEn(updatedDetails.getNameEn());
        existingDetails.setId(updatedDetails.getId());
        existingDetails.setCycle(updatedDetails.getCycle());
        existingDetails.setDefaultSemester(updatedDetails.getDefaultSemester());
        existingDetails.setSemester(updatedDetails.getSemester());
        existingDetails.setCredits(updatedDetails.getCredits());
        existingDetails.setObligationDuration(updatedDetails.getObligationDuration());
        existingDetails.getObligationDuration().setExerciseHours(updatedDetails.getObligationDuration().getExerciseHours());
        existingDetails.getObligationDuration().setSelfLearningHours(updatedDetails.getObligationDuration().getSelfLearningHours());
        existingDetails.getObligationDuration().setProjectHours(updatedDetails.getObligationDuration().getProjectHours());
        existingDetails.getObligationDuration().setHomeworkHours(updatedDetails.getObligationDuration().getHomeworkHours());
        if (existingDetails.getGrading() == null) {
            existingDetails.setGrading(updatedDetails.getGrading());
        } else {
            existingDetails.getGrading().setTestsPoints(updatedDetails.getGrading().getTestsPoints());
            existingDetails.getGrading().setProjectPoints(updatedDetails.getGrading().getProjectPoints());
            existingDetails.getGrading().setActivityPoints(updatedDetails.getGrading().getActivityPoints());
            existingDetails.getGrading().setExamPoints(updatedDetails.getGrading().getExamPoints());
            existingDetails.getGrading().setSignatureCondition(updatedDetails.getGrading().getSignatureCondition());
        }


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
    public void deleteById(String id) {
        subjectDetailsRepository.deleteById(id);
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

        Specification<SubjectDetails> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        if (nameSearch != null && !nameSearch.isEmpty()) {
            spec = spec.and(filterContainsText(SubjectDetails.class, "name", nameSearch));
        }

        if (filteredAccreditation != null && !filteredAccreditation.isEmpty()) {
            spec = spec.and(filterEqualsV(SubjectDetails.class, "accreditation.year", filteredAccreditation));
        }

        return subjectDetailsRepository.findAll(spec, pageRequest);
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
    public List<SubjectNameAndCodeDTO> findAllSubjectNameAndCode() {
        return this.subjectDetailsRepository.findAllNameAndCode()
                .stream()
                .sorted(Comparator.comparing(SubjectNameAndCodeDTO::getName))
                .collect(Collectors.toList());
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
        if (subjectCode.isEmpty() && professorCode.isEmpty() && studyProgramCode.isEmpty() && accreditationYear.isEmpty())
            return new ArrayList<>();

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
        List<String> subjectCodes = studyProgramSubjectProfessors.stream().map(obj -> obj.getStudyProgramSubject().getSubject().getSubject().getId()).distinct().toList();

        List<StudyProgramSubjectProfessorDTO> allData = this.professorRepository.findAllCustomQuery();
        Map<String, List<StudyProgramSubjectProfessorDTO>> allDataMap = allData.stream().collect(Collectors.groupingBy(StudyProgramSubjectProfessorDTO::getSubjectId));

        //Get getSubjectAllocationStatsDTOList
        List<SubjectAllocationStatsDTO> subjectAllocationStatsDTOList = this.subjectDetailsRepository.getSubjectAllocationStatsDTOList();

        List<SubjectStatisticsDTO> resultList = new ArrayList<>();
        subjectCodes.forEach(code -> {
            Optional<SubjectAllocationStatsDTO> subjectAllocationStatsDTO = subjectAllocationStatsDTOList.stream().filter(obj -> obj.getId().equals(code)).findFirst();

            if (allDataMap.get(code) != null) {
                resultList.add(createSubjectStatisticsDTO(allDataMap.get(code), subjectAllocationStatsDTO));
            }
        });

        return resultList;
    }

    private SubjectStatisticsDTO createSubjectStatisticsDTO(List<StudyProgramSubjectProfessorDTO> list, Optional<SubjectAllocationStatsDTO> subjectAllocationStatsDTO) {
        String subjectCode = list.get(0).getSubjectId();
        List<String> professorCodes = list.stream().map(StudyProgramSubjectProfessorDTO::getProfessorId).distinct().toList();
        Integer professorNumber = professorCodes.size();
        List<String> mandatoryStudyPrograms = list.stream()
                .filter(obj -> obj.getStudyProgramMandatory().equals(true))
                .map(StudyProgramSubjectProfessorDTO::getStudyProgramId)
                .distinct()
                .toList();

        long yearsActive = 0L;
        double numberOfFirstTimeStudents = 0.0;
        double numberOfReEnrollmentStudents = 0.0;
        if (subjectAllocationStatsDTO.isPresent()) {
            SubjectAllocationStatsDTO stats = subjectAllocationStatsDTO.get();
            yearsActive = (stats.getYearsActive() != null) ? stats.getYearsActive() : 0L;
            numberOfFirstTimeStudents = (stats.getNumberOfFirstTimeStudents() != null) ? stats.getNumberOfFirstTimeStudents() : 0.0;
            numberOfReEnrollmentStudents = (stats.getNumberOfReEnrollmentStudents() != null) ? stats.getNumberOfReEnrollmentStudents() : 0.0;
        }

        return new SubjectStatisticsDTO(subjectCode, professorCodes, professorNumber, mandatoryStudyPrograms, (int) yearsActive, numberOfFirstTimeStudents, numberOfReEnrollmentStudents);
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
    public Accreditation getActiveAccreditationYear() {
        List<Accreditation> list = this.accreditationRepository.findAll();

        list.removeIf(obj -> obj.getIsActive().equals(false));

        return list.get(0);
    }
}
