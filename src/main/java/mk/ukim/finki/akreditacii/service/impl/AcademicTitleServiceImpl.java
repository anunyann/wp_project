package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.repository.professor.AcademicTitleRepository;
import mk.ukim.finki.akreditacii.service.AcademicTitleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicTitleServiceImpl implements AcademicTitleService {

    private final AcademicTitleRepository academicTitleRepository;

    public AcademicTitleServiceImpl(AcademicTitleRepository academicTitleRepository) {
        this.academicTitleRepository = academicTitleRepository;
    }

    @Override
    public AcademicTitle findById(String id) {
        return academicTitleRepository.findById(id).orElseThrow(() -> new InvalidId(id));
    }

    @Override
    public AcademicTitle save(String id, String institution, ProfessorTitle title, String area, Short electionYear, Short decisionDocumentNumber) {
        AcademicTitle academicTitle = new AcademicTitle(id, institution, title, area, electionYear, decisionDocumentNumber);
        return academicTitleRepository.save(academicTitle);
    }

    @Override
    public void deleteById(String id) {
        academicTitleRepository.deleteById(id);
    }

    @Override
    public List<AcademicTitle> listAll() {
        return academicTitleRepository.findAll();
    }

}
