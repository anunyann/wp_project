package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.model.semester.SemesterType;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

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
    private final ProfessorResumeService professorResumeService;
    private final AccreditationService accreditationService;
    private final ProfessorAccreditationStatsService professorAccreditationStatsService;

    public ProfessorManagementController(AcademicTitleService academicTitleService, EducationService educationService, ProfessorEducationService professorEducationService, ProfessorAcademicTitlesService professorAcademicTitlesService, ProfessorService professorService, ProfessorDetailsService professorDetailsService, ProfessorDeleteService professorDeleteService, ProfessorResumeService professorResumeService, AccreditationService accreditationService, ProfessorAccreditationStatsService professorAccreditationStatsService) {
        this.academicTitleService = academicTitleService;
        this.educationService = educationService;
        this.professorEducationService = professorEducationService;
        this.professorAcademicTitlesService = professorAcademicTitlesService;
        this.professorService = professorService;
        this.professorDetailsService = professorDetailsService;
        this.professorDeleteService = professorDeleteService;
        this.professorResumeService = professorResumeService;
        this.accreditationService = accreditationService;
        this.professorAccreditationStatsService = professorAccreditationStatsService;
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
    public String addProfessor(@RequestParam String name,
                               @RequestParam String id,
                               @RequestParam(required = false) String dateOfBirth,
                               @RequestParam String email,
                               @RequestParam Short orderingRank,
                               @RequestParam ProfessorTitle title,
                               @RequestParam EducationDegree degree) {

        LocalDate dateOfBirthParsed = null;
        if (dateOfBirth != null)
            dateOfBirthParsed = LocalDate.parse(dateOfBirth);
        professorService.save(id, name, email, title, orderingRank);
        ProfessorDetails professorDetails = new ProfessorDetails(id, professorService.getProfessorById(id),
                (float) orderingRank, degree, title.toString(), dateOfBirthParsed, null);
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

    /*RESUME*/

    @GetMapping("/{professorId}/resume/edit")
    public String editResume(@PathVariable String professorId, Model model) {
        Professor professor = this.professorService.getProfessorById(professorId);
        model.addAttribute("professor", professor);

        Optional<ProfessorResume> professorResumeOptional = this.professorResumeService.findById(professorId);
        if (professorResumeOptional.isPresent()) {
            model.addAttribute("professorResume", professorResumeOptional.get());
        } else {
            model.addAttribute("professorResume", new ProfessorResume());
        }
        model.addAttribute("professorId", professorId);

        return "professor/edit_professor_resume";
    }

    @PostMapping("/{professorId}/resume/save")
    public String saveResume(@PathVariable String professorId, @RequestParam(required = false) String resume, @RequestParam(value = "image", required = false) MultipartFile image) {
        this.professorResumeService.save(professorId, resume, image);

        return "redirect:/admin/professor/" + professorId + "/resume";
    }

    @GetMapping("/{professorId}/resume")
    public String viewResume(@PathVariable String professorId, Model model) {
        Professor professor = this.professorService.getProfessorById(professorId);
        model.addAttribute("professor", professor);

        Optional<ProfessorResume> professorResume = this.professorResumeService.findById(professor.getId());
        if (professorResume.isEmpty())
            return "redirect:/admin/professor/" + professorId + "/resume/edit";

        model.addAttribute("professorResume", professorResume.get());

        return "professor/preview_professor_resume";
    }


    @GetMapping("/professor-stats")
    public String stats(Model model,
                        @RequestParam(required = false) String accreditation,
                        @RequestParam(required = false) StudyCycle studyCycle,
                        @RequestParam(required = false) String nameSearch,
                        @RequestParam(required = false) String emailSearch,
                        @RequestParam(required = false) ProfessorTitle titleFilter,
                        @RequestParam(required = false) SemesterType semesterSearch,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer results) {

        String selectedAccreditation;
        StudyCycle selectedCycle = null;
        //deleted accreditation.isEmpty
        if (accreditation == null) {
            selectedAccreditation = accreditationService.findActiveAccreditation().getYear();
        } else {
            selectedAccreditation = accreditation;
        }

        if (studyCycle != null) {
            selectedCycle = studyCycle;
        }

        Page<ProfessorAccreditationStats> stats = professorAccreditationStatsService.findAllWithPaginationAndFilters(pageNum, results, accreditation, studyCycle,
                nameSearch, emailSearch, titleFilter, semesterSearch);
        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("studyCycles", StudyCycle.values());
        model.addAttribute("statsPerProfessors", stats);
        model.addAttribute("selectedAccreditation", selectedAccreditation);
        model.addAttribute("selectedCycle", selectedCycle);
        model.addAttribute("professorTitles", ProfessorTitle.values());
        model.addAttribute("semesterTypes", SemesterType.values());
        model.addAttribute("nameSearch", nameSearch);
        model.addAttribute("emailSearch", emailSearch);
        model.addAttribute("titleFilter", titleFilter);
        model.addAttribute("semesterSearch", semesterSearch);
        return "professor/professor_stats";
    }
}
