package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Education;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.repository.professor.AcademicTitleRepository;
import mk.ukim.finki.akreditacii.repository.professor.EducationRepository;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorAcademicTitlesRepository;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorAcademicTitlesService;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorAcademicTitlesServiceImpl implements ProfessorAcademicTitlesService {

    private final ProfessorAcademicTitlesRepository professorAcademicTitlesRepository;
    private final ProfessorRepository professorRepository;
    private final AcademicTitleRepository academicTitleRepository;
    private final EducationRepository educationRepository;


    public ProfessorAcademicTitlesServiceImpl(ProfessorAcademicTitlesRepository professorAcademicTitlesRepository,
                                              ProfessorRepository professorRepository,
                                              AcademicTitleRepository academicTitleRepository,
                                              EducationRepository educationRepository){
        this.professorAcademicTitlesRepository= professorAcademicTitlesRepository;
        this.professorRepository = professorRepository;
        this.academicTitleRepository = academicTitleRepository;
        this.educationRepository = educationRepository;

    }

    @Override
    public List<ProfessorAcademicTitles> listAll() {
        return professorAcademicTitlesRepository.findAll();
    }
    @Override
    public void deleteByProfessor(Professor professor) {
        professorAcademicTitlesRepository.deleteByProfessor(professor);
    }

    @Override
    public ProfessorAcademicTitles save(String id, Professor professor, AcademicTitle academicTitle) {
        ProfessorAcademicTitles professorAcademicTitles = new ProfessorAcademicTitles(id, professor, academicTitle);
        return professorAcademicTitlesRepository.save(professorAcademicTitles);
    }

    @Override
    public List<Education> listAllEducationDegrees() {
        return educationRepository.findAll();
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
