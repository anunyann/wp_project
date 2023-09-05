package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.EducationDegree;
import mk.ukim.finki.akreditacii.repository.professor.EducationRepository;
import mk.ukim.finki.akreditacii.service.EducationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public Education update(String educationId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area) {
        Education education = educationRepository.findById(educationId).orElse(null);
        education.setDegree(degree);
        education.setFinishingYear(finishingYear);
        education.setInstitution(institution);
        education.setDiscipline(discipline);
        education.setField(field);
        education.setArea(area);

        return educationRepository.save(education);
    }

    @Override
    public Education save(String professorIdValue, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area) {
        Education education = new Education(professorIdValue, degree, finishingYear, institution, discipline, field, area);
        return educationRepository.save(education);
    }

    @Override
    public void deleteById(String id) {
        educationRepository.deleteById(id);
    }

    @Override
    public Education findById(String id) {
        return educationRepository.findById(id).orElseThrow(() -> new InvalidId(id));
    }

    @Override
    public void deleteProfessorEducations(List<String> educationIds) {

        educationRepository.deleteAllByIdIn(educationIds);
    }

}
