package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.EducationDegree;

import java.util.List;

public interface EducationService {

    Education save(String professorId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area);

    Education update(String educationId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area);

    List<Education> listAll();

    void deleteById(String id);

    Education findById(String id);

    void deleteProfessorEducations(List<String> educationIds);

}
