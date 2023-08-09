package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import mk.ukim.finki.akreditacii.service.ProfessorAcademicTitlesService;
import mk.ukim.finki.akreditacii.service.ProfessorDetailsService;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProfessorManagementController {
    private final ProfessorDetailsService professorDetailsService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final ProfessorService professorService;
    public ProfessorManagementController(ProfessorDetailsService professorDetailsService, ProfessorService professorService,ProfessorAcademicTitlesService professorAcademicTitlesService){
        this.professorDetailsService= professorDetailsService;
        this.professorService= professorService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
    }
    @GetMapping(value = {"/professor/{name}"})
    public String professorDetails(@PathVariable String name, Model model){
        Professor professor = professorService.getProfessorById(name);
        model.addAttribute("professor", professorService.getProfessorById(name));

        System.out.println(professor);
        System.out.println(professorAcademicTitlesService.listAll());
        System.out.println(professorAcademicTitlesService.listAllAcademicTitles());
        System.out.println(professorAcademicTitlesService.listAllEducationDegrees());
        return "professor/professor_details";

    }
}
