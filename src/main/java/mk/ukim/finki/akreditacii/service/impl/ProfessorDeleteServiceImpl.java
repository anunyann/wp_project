package mk.ukim.finki.akreditacii.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.model.professor.ProfessorEducation;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessorDeleteServiceImpl implements ProfessorDeleteService {
    private final ProfessorService professorService;
    private final ProfessorEducationService professorEducationService;
    private final EducationService educationService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final AcademicTitleService academicTitleService;
    private final ProfessorDetailsService professorDetailsService;

    public ProfessorDeleteServiceImpl(ProfessorService professorService,
                                      ProfessorEducationService professorEducationService,
                                      EducationService educationService,
                                      ProfessorAcademicTitlesService professorAcademicTitlesService,
                                      AcademicTitleService academicTitleService,
                                      ProfessorDetailsService professorDetailsService

    ) {
        this.professorService = professorService;
        this.professorEducationService = professorEducationService;
        this.educationService = educationService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.academicTitleService = academicTitleService;
        this.professorDetailsService = professorDetailsService;

    }

    @Override
    public void deleteProfessor(String id) {
        Professor professor = professorService.getProfessorById(id);
        List<String> educationIds = getAllEducationIdsForProfessor(professor);
        professorEducationService.deleteAllByProfessor(professor);
        educationService.deleteProfessorEducations(educationIds);

        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professor);
        if (professorAcademicTitles != null) {
            professorAcademicTitlesService.deleteById(professorAcademicTitles.getId());
            academicTitleService.deleteById(professorAcademicTitles.getId());
        }

        professorDetailsService.deleteById(id);
        professorService.deleteById(id);
    }

    public List<String> getAllEducationIdsForProfessor(Professor professor) {
        List<ProfessorEducation> professorEducations = professorEducationService.listEducationByProfessor(professor);
        return professorEducations.stream().map(ProfessorEducation::getId).collect(Collectors.toList());
    }


}
