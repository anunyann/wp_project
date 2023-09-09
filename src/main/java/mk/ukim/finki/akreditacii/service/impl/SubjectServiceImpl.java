package mk.ukim.finki.akreditacii.service.impl;

import jakarta.persistence.EntityNotFoundException;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidSubjectId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.repository.SubjectDetailsRepository;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


}
