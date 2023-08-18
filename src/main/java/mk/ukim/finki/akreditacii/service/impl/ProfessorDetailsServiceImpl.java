package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.EducationDegree;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorDetailsRepository;
import mk.ukim.finki.akreditacii.service.ProfessorDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfessorDetailsServiceImpl implements ProfessorDetailsService {

    private final ProfessorDetailsRepository professorDetailsRepository;

    public ProfessorDetailsServiceImpl(ProfessorDetailsRepository professorDetailsRepository){
        this.professorDetailsRepository = professorDetailsRepository;
    }

    @Override
    public ProfessorDetails findProfessor(Professor professor) {
        return professorDetailsRepository.findByProfessor(professor);
    }

    @Override
    public void deleteById(String id) {
        professorDetailsRepository.deleteById(id);
    }

    @Override
    public ProfessorDetails save(ProfessorDetails professorDetails) {
        return professorDetailsRepository.save(professorDetails);
    }

    @Override
    public ProfessorDetails findById(String id) {
        return professorDetailsRepository.findById(id).orElse(null);
    }
    @Override
    public ProfessorDetails findProfessorById(String id) {
        return professorDetailsRepository.findByProfessorId(id);
    }

    @Override
    public List<ProfessorDetails> findAll() {
        return professorDetailsRepository.findAll();
    }
}
