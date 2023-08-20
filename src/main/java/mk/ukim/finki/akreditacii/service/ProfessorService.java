package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;

import java.util.List;

public interface ProfessorService {

    Professor getProfessorById(String professorId);
    List<Professor> listAll();
    Professor save(String id, String name,String email, ProfessorTitle title);
    List<Professor> getProfessors();

    void deleteById(String id);
}
