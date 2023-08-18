package mk.ukim.finki.akreditacii.web;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProfessorManagementController {

    private final AcademicTitleService academicTitleService;
    private final EducationService educationService;
    private final ProfessorEducationService professorEducationService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final ProfessorService professorService;
    private final ProfessorDetailsService professorDetailsService;

    public ProfessorManagementController(
                                         ProfessorService professorService,
                                         ProfessorAcademicTitlesService professorAcademicTitlesService,
                                         AcademicTitleService academicTitleService,
                                         ProfessorEducationService professorEducationService,
                                         EducationService educationService,
                                         ProfessorDetailsService professorDetailsService
                                      ){

        this.professorService= professorService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.academicTitleService = academicTitleService;
        this.educationService = educationService;
        this.professorEducationService = professorEducationService;
        this.professorDetailsService = professorDetailsService;

    }



    @GetMapping(value = {"/professor/{name}"})
    public String professorDetails(@PathVariable String name, Model model){
        Professor professor = professorService.getProfessorById(name);
        ProfessorDetails professorDetails =  professorDetailsService.findById(name);
        model.addAttribute("professorDetails", professorDetails);

        model.addAttribute("professor", professor);
        model.addAttribute("educations", professorEducationService.listEducationByProfessor(professor));
        return "professor/professor_details";

    }

    @GetMapping(value = {"/professor"})
    public String listAllProfessors(Model model){
        model.addAttribute("professors", professorService.listAll());
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

        return "redirect:/professor";
    }

    @GetMapping(value = {"/professor/{id}/education"})
    public String addEducation (@PathVariable String id, Model model){
        model.addAttribute("professorIdValue", id);
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_education";
    }
    @PostMapping("/professor/{id}/education")
    public String saveEducation(
            @PathVariable String id,
            @RequestParam("degree") EducationDegree degree,
            @RequestParam("finishingYear") Short finishingYear,
            @RequestParam("institution") String institution,
            @RequestParam("discipline") String discipline,
            @RequestParam("field") String field,
            @RequestParam("area") String area) {

        Education education = educationService.save(id, degree,finishingYear,institution, discipline, field,area);
        Professor professor = professorService.getProfessorById(id);
        professorEducationService.save(professor, education, 1F);

        return "redirect:/professor/" + id;
    }

    @GetMapping("/professor/{professorId}/education/{educationId}/delete")
    public String deleteEducation(@PathVariable String professorId, @PathVariable String educationId) {
        ProfessorEducation professorEducation = professorEducationService.findByEducationId(educationId);;
        professorEducationService.deleteById(professorEducation.getId());
        educationService.deleteById(educationId);

        return "redirect:/professor/"+professorId;
    }



    @GetMapping(value = {"/professor/add-professor"})
    public String addProfessor (Model model){
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_professor";
    }

    @PostMapping("/professor/add-professor")
    public String addProfessor(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String id,
            @RequestParam String dateOfBirth,
            @RequestParam String email,
            @RequestParam ProfessorTitle title,
            @RequestParam EducationDegree degree) {

        LocalDate dateOfBirthParsed = LocalDate.parse(dateOfBirth);
        Professor professor = new Professor(id, firstName + " "+ lastName, email, title);
        professorService.save(id, firstName, lastName, email, title);
        ProfessorDetails professorDetails = new ProfessorDetails(id, professor,12F,degree,title.toString(),dateOfBirthParsed,null);
        professorDetailsService.save(professorDetails);


        return "redirect:/professor";
    }

    @Transactional
    @GetMapping("/professor/delete/{id}")
    public String deleteProfessor(@PathVariable String id) {

        Professor professor = professorService.getProfessorById(id);
        professorAcademicTitlesService.deleteByProfessor(professor);

        this.professorDetailsService.deleteById(id);
        this.professorService.deleteById(id);

        return "redirect:/professor";
    }
}
