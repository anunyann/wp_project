package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;

import java.util.List;

public interface ProfessorAcademicTitlesService {

    ProfessorAcademicTitles findByProfessor(Professor professor);
    List<ProfessorAcademicTitles> listAll();
    ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle);
    List<Education> listAllEducationDegrees();
    void deleteByProfessor(Professor professor);


}