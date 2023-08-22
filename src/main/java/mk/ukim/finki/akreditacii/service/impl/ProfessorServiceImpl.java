package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl( ProfessorRepository professorRepository){
         this.professorRepository = professorRepository;
    }

    @Override
    public Professor getProfessorById(String professorId) {
        return professorRepository.findById(professorId).orElseThrow(() -> new InvalidId(professorId));
    }

    @Override
    public Professor save(String id, String name,String email, ProfessorTitle title) {
        Professor professor = new Professor(id, name, email, title);
        return professorRepository.save(professor);
    }

    @Override
    public List<Professor> listAll() {
        return professorRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
         professorRepository.deleteById(id);
    }
}
