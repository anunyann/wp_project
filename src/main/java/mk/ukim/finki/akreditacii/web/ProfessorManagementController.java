package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("admin/professor")
public class ProfessorManagementController {
    private final AcademicTitleService academicTitleService;
    private final EducationService educationService;
    private final ProfessorEducationService professorEducationService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final ProfessorService professorService;
    private final ProfessorDetailsService professorDetailsService;
    private final ProfessorDeleteService professorDeleteService;

    public ProfessorManagementController(ProfessorService professorService, ProfessorAcademicTitlesService professorAcademicTitlesService, AcademicTitleService academicTitleService, ProfessorEducationService professorEducationService, EducationService educationService, ProfessorDetailsService professorDetailsService, ProfessorDeleteService professorDeleteService) {

        this.professorService = professorService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.academicTitleService = academicTitleService;
        this.educationService = educationService;
        this.professorEducationService = professorEducationService;
        this.professorDetailsService = professorDetailsService;
        this.professorDeleteService = professorDeleteService;
    }

    @GetMapping(value = {"/{professorId}"})
    public String professorDetails(@PathVariable String professorId, Model model) {
        Professor professor = professorService.getProfessorById(professorId);
        ProfessorDetails professorDetails = professorDetailsService.findById(professorId);
        model.addAttribute("professorDetails", professorDetails);
        model.addAttribute("professor", professor);
        model.addAttribute("educations", professorEducationService.listEducationByProfessor(professor));

        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professorService.getProfessorById(professorId));
        if (professorAcademicTitles == null) {
            model.addAttribute("academicTitle", null);
        } else {
            model.addAttribute("academicTitle", professorAcademicTitles.getAcademicTitle());
        }

        return "professor/professor_details";

    }

    @GetMapping
    public String pageableProfessor(Model model,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer results,
                                    @RequestParam(required = false) String searchString,
                                    @RequestParam(required = false) String titleFilter
    ) {
        Page<Professor> professorPage;


        if (searchString == null && titleFilter == null) {
            professorPage = professorService
                    .findAllWithPagination(pageNum, results);
        } else {
            professorPage = professorService
                    .findAllWithPaginationFiltered(pageNum, results,
                            searchString, titleFilter);

            model.addAttribute("searchString", searchString);
            model.addAttribute("titleFilter", titleFilter);

        }
        model.addAttribute("professorPage", professorPage);
        model.addAttribute("professorTitles", ProfessorTitle.values());

        return "professor/professor_list";
    }


    @GetMapping(value = {"/add-professor"})
    public String addProfessor(Model model) {
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_professor";
    }

    @GetMapping(value = {"/{id}/edit"})
    public String editProfessor(@PathVariable String id, Model model) {

        Professor professor = professorService.getProfessorById(id);
        ProfessorDetails professorDetails = professorDetailsService.findById(id);
        model.addAttribute("professor", professor);
        model.addAttribute("professorDetails", professorDetails);
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_professor";
    }

    @PostMapping("/add-professor")
    public String addProfessor(@RequestParam String name, @RequestParam String id, @RequestParam String dateOfBirth, @RequestParam String email, @RequestParam ProfessorTitle title, @RequestParam EducationDegree degree) {

        LocalDate dateOfBirthParsed = LocalDate.parse(dateOfBirth);
        professorService.save(id, name, email, title);
        ProfessorDetails professorDetails = new ProfessorDetails(id, professorService.getProfessorById(id), 1F, degree, title.toString(), dateOfBirthParsed, null);
        professorDetailsService.save(professorDetails);


        return "redirect:/admin/professor";
    }

    @GetMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable String id) {

        professorDeleteService.deleteProfessor(id);

        return "redirect:/admin/professor";
    }

    /* EDUCATIONS*/

    @GetMapping("/{professorId}/education/add")
    public String addEducation(@PathVariable String professorId, Model model) {
        model.addAttribute("professorId", professorId);
        model.addAttribute("educationDegrees", EducationDegree.values());

        return "professor/add_education";
    }

    @GetMapping("/{professorId}/education/{educationId}/edit")
    public String editEducation(@PathVariable String professorId, @PathVariable String educationId, Model model) {

        Education education = educationService.findById(educationId);
        System.out.println(education);
        model.addAttribute("professorId", professorId);
        model.addAttribute("educationDegrees", EducationDegree.values());
        model.addAttribute("education", education);

        return "professor/add_education";
    }

    @PostMapping("/{professorId}/education")
    public String saveOrUpdateEducation(@PathVariable String professorId, @RequestParam("educationId") String educationId, @RequestParam("degree") EducationDegree degree, @RequestParam("finishingYear") Short finishingYear, @RequestParam("institution") String institution, @RequestParam("discipline") String discipline, @RequestParam("field") String field, @RequestParam("area") String area) {

        if (educationId == "") {
            Education education = educationService.save(professorId, degree, finishingYear, institution, discipline, field, area);
            Professor professor = professorService.getProfessorById(professorId);
            professorEducationService.save(professor, education, 1F);
        } else {
            educationService.update(educationId, degree, finishingYear, institution, discipline, field, area);
        }

        return "redirect:/admin/professor/" + professorId;
    }

    @GetMapping("/{professorId}/education/{educationId}/delete")
    public String deleteEducation(@PathVariable String professorId, @PathVariable String educationId) {

        ProfessorEducation professorEducation = professorEducationService.findByEducationId(educationId);
        professorEducationService.deleteById(professorEducation.getId());
        educationService.deleteById(educationId);

        return "redirect:/admin/professor/" + professorId;
    }


    /*ACADEMIC TITTLE*/

    @GetMapping(value = {"/{professorId}/titles"})
    public String listAcademicTitle(@PathVariable String professorId, Model model) {
        model.addAttribute("professorTitles", ProfessorTitle.values());
        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByProfessor(professorService.getProfessorById(professorId));
        if (professorAcademicTitles == null) return "professor/add_academic_title";

        model.addAttribute("academicTitle", professorAcademicTitles.getAcademicTitle());

        return "professor/add_academic_title";

    }

    @PostMapping("/{professorId}/titles")
    public String saveAcademicTitle(@PathVariable String professorId, @RequestParam("institution") String institution, @RequestParam("title") ProfessorTitle title, @RequestParam("area") String area, @RequestParam("electionYear") Short electionYear, @RequestParam("decisionDocumentNumber") Short decisionDocumentNumber, Model model) {

        AcademicTitle academicTitle = academicTitleService.save(professorId + decisionDocumentNumber, institution, title, area, electionYear, decisionDocumentNumber);
        Professor professor = professorService.getProfessorById(professorId);
        professorAcademicTitlesService.save(professorId, professor, academicTitle);


        return "redirect:/admin/professor/" + professorId;
    }

    @GetMapping("/{professorId}/titles/{titleId}/delete")
    public String deleteTitle(@PathVariable String professorId, @PathVariable String titleId) {
        ProfessorAcademicTitles professorAcademicTitles = professorAcademicTitlesService.findByTitleId(titleId);
        professorAcademicTitlesService.deleteById(professorAcademicTitles.getId());
        academicTitleService.deleteById(titleId);

        return "redirect:/admin/professor/" + professorId;
    }


}
