package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Professor;

import java.util.List;

public interface ProfessorService {

    Professor getProfessorById(String professorId);
    List<Professor> listAll();

    List<Professor> getProfessors();
}
