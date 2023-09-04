package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorAcademicTitlesRepository;
import mk.ukim.finki.akreditacii.service.ProfessorAcademicTitlesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorAcademicTitlesServiceImpl implements ProfessorAcademicTitlesService {

    private final ProfessorAcademicTitlesRepository professorAcademicTitlesRepository;


    public ProfessorAcademicTitlesServiceImpl(ProfessorAcademicTitlesRepository professorAcademicTitlesRepository) {
        this.professorAcademicTitlesRepository = professorAcademicTitlesRepository;
    }

    @Override
    public List<ProfessorAcademicTitles> listAll() {
        return professorAcademicTitlesRepository.findAll();
    }

    @Override
    public ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle) {
        ProfessorAcademicTitles professorAcademicTitles = new ProfessorAcademicTitles(id, professor, academicTitle);
        return professorAcademicTitlesRepository.save(professorAcademicTitles);
    }

    @Override
    public ProfessorAcademicTitles findByProfessor(Professor professor) {
        return professorAcademicTitlesRepository.findByProfessor(professor);
    }

    @Override
    public ProfessorAcademicTitles findByTitleId(String id) {
        return professorAcademicTitlesRepository.findByAcademicTitleId(id);
    }

    @Override
    public void deleteById(String id) {
        professorAcademicTitlesRepository.deleteById(id);
    }
}
