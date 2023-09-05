package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;

public interface ProfessorAcademicTitlesService {

    ProfessorAcademicTitles findByProfessor(Professor professor);

    ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle);

    ProfessorAcademicTitles findByTitleId(String id);

    void deleteById(String id);

}