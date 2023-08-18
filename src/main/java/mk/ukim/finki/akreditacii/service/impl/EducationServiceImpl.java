package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.EducationDegree;
import mk.ukim.finki.akreditacii.repository.professor.EducationRepository;
import mk.ukim.finki.akreditacii.service.EducationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    public EducationServiceImpl (EducationRepository educationRepository){
        this.educationRepository = educationRepository;
    }

    @Override
    public Education save(String professorIdValue,EducationDegree degree,Short finishingYear,String institution,String  discipline,String  field,String area) {
        Education education = new Education(professorIdValue+finishingYear, degree,finishingYear,institution, discipline, field,area);

        return educationRepository.save(education);
    }

    @Override
    public void deleteById(String id) {
        educationRepository.deleteById(id);
    }

    @Override
    public void deleteProfessorEducations(List<String> educationIds) {
        educationRepository.deleteAllByIdIn(educationIds);
    }

    @Override
    public List<Education> listAll() {
        return educationRepository.findAll();
    }
}
