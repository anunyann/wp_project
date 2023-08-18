package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.EducationDegree;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;

import java.time.LocalDate;
import java.util.List;

public interface ProfessorDetailsService {

    ProfessorDetails save(ProfessorDetails professorDetails);

    ProfessorDetails findById(String id);
    ProfessorDetails findProfessorById(String id);
    ProfessorDetails findProfessor(Professor professor);
    List<ProfessorDetails> findAll();
    void deleteById(String id);
}
