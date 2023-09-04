package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorEducation;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorEducationRepository;
import mk.ukim.finki.akreditacii.service.ProfessorEducationService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProfessorEducationServiceImpl implements ProfessorEducationService {

    private final ProfessorEducationRepository professorEducationRepository;

    public ProfessorEducationServiceImpl( ProfessorEducationRepository professorEducationRepository){
        this.professorEducationRepository = professorEducationRepository;
    }

    @Override
    public List<ProfessorEducation> listEducationByProfessor(Professor professor) {
        return professorEducationRepository.findAllByProfessor(professor);
    }

    @Override
    public List<ProfessorEducation> listAll() {
        return professorEducationRepository.findAll();
    }

    @Override
    public void deleteAllByProfessor(Professor professor) {
        professorEducationRepository.deleteAllByProfessor(professor);
    }

    @Override
    public void deleteById(String id) {
        professorEducationRepository.deleteById(id);
    }

    @Override
    public ProfessorEducation findByEducationId(String id) {
        return professorEducationRepository.findByEducationId(id);
    }


    @Override
    public ProfessorEducation save( Professor professor, Education education, Float order){
        ProfessorEducation professorEducation = new ProfessorEducation(education.getId(),professor, education, order );

        return professorEducationRepository.save(professorEducation);
    }
}
