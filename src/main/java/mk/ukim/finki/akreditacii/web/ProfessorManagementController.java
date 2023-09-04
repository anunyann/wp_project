package mk.ukim.finki.akreditacii.web;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
                                      ) {

        this.professorService = professorService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.academicTitleService = academicTitleService;
        this.educationService = educationService;
        this.professorEducationService = professorEducationService;
        this.professorDetailsService = professorDetailsService;
    }

    @GetMapping(value = {"/professor/{professorId}"})
    public String professorDetails(@PathVariable String professorId, Model model){
        Professor professor = professorService.getProfessorById(professorId);
        ProfessorDetails professorDetails =  professorDetailsService.findById(professorId);
        model.addAttribute("professorDetails", professorDetails);
        model.addAttribute("professor", professor);
        model.addAttribute("educations", professorEducationService.listEducationByProfessor(professor));

        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professorService.getProfessorById(professorId));
        if(professorAcademicTitles== null ) {
            model.addAttribute("academicTitle", null);
        }else  {
            model.addAttribute("academicTitle", professorAcademicTitles.getAcademicTitle());
        }

        return "professor/professor_details";

    }

    @GetMapping(value = {"/professor"})
    public String listAllProfessors(Model model){
        model.addAttribute("professors", professorService.listAll());
        return "professor/professor_list";
    }

    @GetMapping(value = {"/professor/add-professor"})
    public String addProfessor (Model model){
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_professor";
    }

    @GetMapping(value = {"/professor/{id}/edit"})
    public String editProfessor (@PathVariable String id, Model model){

        Professor professor = professorService.getProfessorById(id);
        ProfessorDetails professorDetails = professorDetailsService.findById(id);
        model.addAttribute("professor", professor);
        model.addAttribute("professorDetails", professorDetails);
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_professor";
    }
    @PostMapping("/professor/add-professor")
    public String addProfessor(
            @RequestParam String name,
            @RequestParam String id,
            @RequestParam String dateOfBirth,
            @RequestParam String email,
            @RequestParam ProfessorTitle title,
            @RequestParam EducationDegree degree) {

        LocalDate dateOfBirthParsed = LocalDate.parse(dateOfBirth);
        professorService.save(id, name, email, title);
        ProfessorDetails professorDetails = new ProfessorDetails(id, professorService.getProfessorById(id),1F,degree,title.toString(),dateOfBirthParsed,null);
        professorDetailsService.save(professorDetails);


        return "redirect:/professor";
    }

    @Transactional
    @GetMapping("/professor/{id}/delete")
    public String deleteProfessor(@PathVariable String id) {

        Professor professor = professorService.getProfessorById(id);

        List<String> educationIds = getAllEducationIdsForProfessor(professor);
        professorEducationService.deleteAllByProfessor(professor);
        educationService.deleteProfessorEducations(educationIds);

        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professor);
        if( professorAcademicTitles != null){
            professorAcademicTitlesService.deleteById(professorAcademicTitles.getId());
            academicTitleService.deleteById(professorAcademicTitles.getId());
        }

        professorDetailsService.deleteById(id);
        professorService.deleteById(id);

        return "redirect:/professor";
    }

    /* EDUCATIONS*/

    @GetMapping("/professor/{professorId}/education/add")
    public String addEducation (@PathVariable String professorId, Model model){
        model.addAttribute("professorId", professorId);
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_education";
    }
    @GetMapping("/professor/{professorId}/education/{educationId}/edit")
    public String editEducation (@PathVariable String professorId, @PathVariable String educationId, Model model){

        Education education = educationService.findById(educationId);

        model.addAttribute("professorId", professorId);
        model.addAttribute("educationDegrees", EducationDegree.values());
        model.addAttribute("education", education);

        return "professor/add_education";
    }
    @PostMapping("/professor/{professorId}/education")
    public String saveOrUpdateEducation(
            @PathVariable String professorId,
            @RequestParam("educationId") String educationId,
            @RequestParam("degree") EducationDegree degree,
            @RequestParam("finishingYear") Short finishingYear,
            @RequestParam("institution") String institution,
            @RequestParam("discipline") String discipline,
            @RequestParam("field") String field,
            @RequestParam("area") String area) {

        if (educationId=="") {
            Education education = educationService.save(professorId, degree, finishingYear, institution, discipline, field, area);
            Professor professor = professorService.getProfessorById(professorId);
            professorEducationService.save(professor, education, 1F);
        } else {
            educationService.update(educationId,degree,finishingYear,institution,discipline,field,area);
        }

        return "redirect:/professor/" + professorId;
    }

    @GetMapping("/professor/{professorId}/education/{educationId}/delete")
    public String deleteEducation(@PathVariable String professorId, @PathVariable String educationId) {

        ProfessorEducation professorEducation = professorEducationService.findByEducationId(educationId);;
        professorEducationService.deleteById(professorEducation.getId());
        educationService.deleteById(educationId);

        return "redirect:/professor/"+professorId;
    }

    public List<String> getAllEducationIdsForProfessor(Professor professor) {
        List<ProfessorEducation> professorEducations = professorEducationService.listEducationByProfessor(professor);
        return professorEducations.stream()
                .map(ProfessorEducation::getId)
                .collect(Collectors.toList());
    }

    /*ACADEMIC TITTLE*/

    @GetMapping(value = {"/professor/{professorId}/titles"})
    public String listAcademicTitle(@PathVariable String professorId,Model model){
        model.addAttribute("professorTitles", ProfessorTitle.values());
        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professorService.getProfessorById(professorId));
        if(professorAcademicTitles== null)
            return "professor/add_academic_title";

        model.addAttribute("academicTitle", professorAcademicTitles.getAcademicTitle());

        return "professor/add_academic_title";

    }
    @PostMapping("/professor/{professorId}/titles")
    public String saveAcademicTitle(
            @PathVariable String professorId,
            @RequestParam("institution") String institution,
            @RequestParam("title") ProfessorTitle title,
            @RequestParam("area") String area,
            @RequestParam("electionYear") Short electionYear,
            @RequestParam("decisionDocumentNumber") Short decisionDocumentNumber,
            Model model) {

        AcademicTitle academicTitle = academicTitleService.save(professorId+decisionDocumentNumber, institution, title, area, electionYear, decisionDocumentNumber);
        Professor professor = professorService.getProfessorById(professorId);
        professorAcademicTitlesService.save(professorId, professor, academicTitle);


        return "redirect:/professor/"+ professorId;
    }

    @GetMapping("/professor/{professorId}/titles/{titleId}/delete")
    public String deleteTitle(@PathVariable String professorId, @PathVariable String titleId) {
        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByTitleId(titleId);;
        professorAcademicTitlesService.deleteById(professorAcademicTitles.getId());
        academicTitleService.deleteById(titleId);

        return "redirect:/professor/"+professorId;
    }


}
