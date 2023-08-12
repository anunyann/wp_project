package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.repository.professor.AcademicTitleRepository;
import mk.ukim.finki.akreditacii.service.AcademicTitleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicTitleServiceImpl implements AcademicTitleService {

    private final AcademicTitleRepository academicTitleRepository;
    public AcademicTitleServiceImpl (AcademicTitleRepository academicTitleRepository){
        this.academicTitleRepository = academicTitleRepository;
    }

    @Override
    public Optional<AcademicTitle> save(String id,String institution, ProfessorTitle title, String area, Short electionYear, Short decisionDocumentNumber) {
        if(academicTitleRepository.findById(id).isPresent()){
            return Optional.of(academicTitleRepository.findById(id).get());
        }
        AcademicTitle academicTitle = new AcademicTitle(id,institution,title, area, electionYear,decisionDocumentNumber);
        return Optional.of(academicTitleRepository.save(academicTitle));
    }
    @Override
    public List<AcademicTitle> listAll() {
        return academicTitleRepository.findAll();
    }

}
