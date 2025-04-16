package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;

import mk.ukim.finki.akreditacii.service.*;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("admin/study-programs")
@Controller
public class StudyProgramManagement {

    private final StudyProgramDetailsService studyProgramDetailsService;
    private final AccreditationService accreditationService;
    private final ProfessorService professorService;

    public StudyProgramManagement(StudyProgramDetailsService studyProgramDetailsService, AccreditationService accreditationService, ProfessorService professorService) {
        this.studyProgramDetailsService = studyProgramDetailsService;
        this.accreditationService = accreditationService;
        this.professorService = professorService;
    }

    @GetMapping
    public String pageableStudyProgramsWithFilters(Model model,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer results,
                                                   @RequestParam(required = false) String nameSearch,
                                                   @RequestParam(required = false) String filteredAccreditation,
                                                   @RequestParam(required = false) String filteredStudyCycle,
                                                   @RequestParam(required = false) String filteredDurationInYears,
                                                   @RequestParam(required = false) String filteredOnEnglish) {

        Page<StudyProgramDetails> studyProgramDetailsPage;
        String selectedAccreditationYear;
        if (filteredAccreditation == null) {
            selectedAccreditationYear = this.accreditationService.findActiveAccreditation().getYear();
            nameSearch = "";
            filteredStudyCycle = "";
            filteredDurationInYears = "";
            studyProgramDetailsPage = studyProgramDetailsService
                    .findAllWithPaginationFiltered(pageNum, results,
                            nameSearch, selectedAccreditationYear, filteredStudyCycle,
                            filteredDurationInYears, filteredOnEnglish);
        } else {
            selectedAccreditationYear = filteredAccreditation;
            studyProgramDetailsPage = studyProgramDetailsService
                    .findAllWithPaginationFiltered(pageNum, results,
                            nameSearch, selectedAccreditationYear, filteredStudyCycle,
                            filteredDurationInYears, filteredOnEnglish);
        }

        System.out.println(studyProgramDetailsPage.getContent());
        model.addAttribute("text", nameSearch);
        model.addAttribute("accreditation", selectedAccreditationYear);
        model.addAttribute("studyCycle", filteredStudyCycle);
        model.addAttribute("durationInYears", filteredDurationInYears);
        model.addAttribute("onEnglish", filteredOnEnglish != null);

        model.addAttribute("studyProgramDetailsPage", studyProgramDetailsPage);
        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("studyCycles", StudyCycle.values());

        return "study_program/study_program_list";
    }

    @GetMapping("/add-form")
    public String addStudyProgram(Model model) {
        model.addAttribute("studyCycles", StudyCycle.values());
        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("professors", professorService.findAll());
        return "study_program/add_study_program.html";
    }

    @GetMapping("/edit-form/{id}")
    public String editStudyProgram(Model model, @PathVariable String id) {
        Optional<StudyProgramDetails> optionalStudyProgramDetails = studyProgramDetailsService.findById(id);
        if (optionalStudyProgramDetails.isPresent()) {
            StudyProgramDetails studyProgramDetails = optionalStudyProgramDetails.get();
            model.addAttribute("studyProgramDetails", studyProgramDetails);
            model.addAttribute("studyCycles", StudyCycle.values());
            model.addAttribute("accreditations", accreditationService.findAll());
            model.addAttribute("professors", professorService.findAll());
            return "study_program/add_study_program.html";
        }
        return null;
    }

    @PostMapping("/add")
    public String add(Model model,
                      @RequestParam String code,
                      @RequestParam String name,
                      @RequestParam String nameEn,
                      @RequestParam String accreditationId,
                      @RequestParam StudyCycle studyCycle,
                      @RequestParam(required = false) Float order,
                      @RequestParam(required = false) Short durationYears,
                      @RequestParam(required = false) Short durationSemesters,
                      @RequestParam(required = false) String generalInformation,
                      @RequestParam(required = false) String graduationTitle,
                      @RequestParam(required = false) String graduationTitleEn,
                      @RequestParam(required = false) String subjectRestrictions,
                      @RequestParam(required = false) String onEnglish,
                      @RequestParam(required = false) String bilingual,
                      @RequestParam(required = false) String professor) {

        Professor coordinator = null;
        if (professor != null) {
            coordinator = professorService.getProfessorById(professor);
        }

        studyProgramDetailsService.save(code, name, nameEn, order, durationYears, durationSemesters, generalInformation,
                graduationTitle, graduationTitleEn, subjectRestrictions, onEnglish != null, studyCycle,
                accreditationService.findById(accreditationId), bilingual != null,
                coordinator);
        return "redirect:/admin/study-programs";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudyProgram(@PathVariable String id) {
        this.studyProgramDetailsService.deleteById(id);
        return "redirect:/admin/study-programs";
    }

}
