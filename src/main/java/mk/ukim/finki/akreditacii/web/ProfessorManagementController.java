package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProfessorManagementController {
    private final ProfessorDetailsService professorDetailsService;
    private final AcademicTitleService academicTitleService;
    private final EducationService educationService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final ProfessorService professorService;
    public ProfessorManagementController(ProfessorDetailsService professorDetailsService,
                                         ProfessorService professorService,
                                         ProfessorAcademicTitlesService professorAcademicTitlesService,
                                         AcademicTitleService academicTitleService,
                                         EducationService educationService){
        this.professorDetailsService= professorDetailsService;
        this.professorService= professorService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.academicTitleService = academicTitleService;
        this.educationService = educationService;
    }



    @GetMapping(value = {"/professor/{name}"})
    public String professorDetails(@PathVariable String name, Model model){
        model.addAttribute("professor", professorService.getProfessorById(name));

        return "professor/professor_details";

    }

    @GetMapping(value = {"/professor"})
    public String listAllProfessors(Model model){

        model.addAttribute("professors", professorService.listAll());
        System.out.println(academicTitleService.listAll());
        System.out.println(educationService.listAll());
        return "professor/professor_list";

    }
    @GetMapping(value = {"/professor/add-academic-title"})
    public String addAcademicTitle(Model model){
        model.addAttribute("professorTitles", ProfessorTitle.values());
        return "professor/add_academic_title";

    }
    @PostMapping("/professor/add-academic-title")
    public String saveAcademicTitle(
            @RequestParam("id") String id,
            @RequestParam("institution") String institution,
            @RequestParam("title") ProfessorTitle title,
            @RequestParam("area") String area,
            @RequestParam("electionYear") Short electionYear,
            @RequestParam("decisionDocumentNumber") Short decisionDocumentNumber,
            Model model) {

        academicTitleService.save(id,institution,title,area,electionYear,decisionDocumentNumber);
        return "redirect:/professor";
    }

    @GetMapping(value = {"/professor/add-education"})
    public String addEducation (Model model){

        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_education";

    }
    @PostMapping("/professor/add-education")
    public String saveEducation(
            @RequestParam("id") String id,
            @RequestParam("degree") EducationDegree degree,
            @RequestParam("finishingYear") Short finishingYear,
            @RequestParam("institution") String institution,
            @RequestParam("discipline") String discipline,
            @RequestParam("field") String field,
            @RequestParam("area") String area,
            Model model) {
        educationService.save(id,degree,finishingYear,institution,discipline,field,area);

        return "redirect:/professor";
    }
}
