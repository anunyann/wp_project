package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import mk.ukim.finki.akreditacii.service.StudyProgramSubjectProfessorService;
import mk.ukim.finki.akreditacii.service.StudyProgramSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyProgramSubjectProfessorImpl implements StudyProgramSubjectProfessorService {
    private final StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository;
    private final StudyProgramSubjectService studyProgramSubjectService;
    private final ProfessorService professorService;


    public StudyProgramSubjectProfessorImpl(StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository, StudyProgramSubjectService studyProgramSubjectService, ProfessorService professorService) {
        this.studyProgramSubjectProfessorRepository = studyProgramSubjectProfessorRepository;
        this.studyProgramSubjectService = studyProgramSubjectService;
        this.professorService = professorService;
    }



    @Override
    public List<Professor> professorsNotOnSubject(String subjectId) {
        return studyProgramSubjectProfessorRepository.findProfessorsNotAssignedForSubject(subjectId);
    }



    @Override
    public List<Professor> findProfessorsFromOtherProgramsForSubject(String subjectId, String currentProgramCode) {
        return studyProgramSubjectProfessorRepository.findProfessorsFromOtherProgramsForSubject(subjectId,currentProgramCode);
    }

    @Override
    public void deleteProfessorForSubject(Professor professor, StudyProgramSubject studyProgramSubject) {
        studyProgramSubjectProfessorRepository.deleteStudyProgramSubjectProfessorByProfessorAndStudyProgramSubject(professor,studyProgramSubject);
    }





    @Override
    public StudyProgramSubjectProfessor save(String id, String studyProgramSubjectId, String professorId, Float newOrder) {
        StudyProgramSubject studyProgramSubject = studyProgramSubjectService.findById(studyProgramSubjectId);
        Professor professor = professorService.getProfessorById(professorId);

        StudyProgramSubjectProfessor studyProgramSubjectProfessor = new StudyProgramSubjectProfessor();
        studyProgramSubjectProfessor.setId(id);
        studyProgramSubjectProfessor.setStudyProgramSubject(studyProgramSubject);
        studyProgramSubjectProfessor.setProfessor(professor);
        studyProgramSubjectProfessor.setOrder(newOrder);

        return studyProgramSubjectProfessorRepository.save(studyProgramSubjectProfessor);
    }



    @Override
    public List<StudyProgramSubjectProfessor> professorsFromStudyProgramSubject(StudyProgramSubject studyProgramSubject) {
        return studyProgramSubjectProfessorRepository.findAllByStudyProgramSubject(studyProgramSubject);
    }

    @Override
    public List<StudyProgramSubjectProfessor> saveAll(List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList) {
       return  studyProgramSubjectProfessorRepository.saveAll(studyProgramSubjectProfessorList);
    }

    @Override
    public List<Professor> professorsOnAStudyProgram(String programCode) {
        return studyProgramSubjectProfessorRepository.findProfessorsByStudyProgramCode(programCode);
    }



}
