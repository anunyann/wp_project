package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidProfessorId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.akreditacii.model.professor.ProfessorTitle.PROFESSOR;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl( ProfessorRepository professorRepository){
         this.professorRepository = professorRepository;
    }

    @Override
    public Professor getProfessorById(String professorId) {
        return professorRepository.findById(professorId).orElseThrow(() -> new InvalidProfessorId(professorId));
    }

    @Override
    public List<Professor> listAll() {
        return professorRepository.findAll();
    }

    @Override
    public List<Professor> getProfessors() {
        return professorRepository.findAllByTitleLike(PROFESSOR);
    }
}
