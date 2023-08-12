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
    public Optional<Education> save(String id, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area) {
        if(educationRepository.findById(id).isPresent()){
            return Optional.of(educationRepository.findById(id).get());
        }
        Education education = new Education(id, degree,finishingYear,institution,discipline,field,area);

        return Optional.of(educationRepository.save(education));
    }

    @Override
    public List<Education> listAll() {
        return educationRepository.findAll();
    }
}
