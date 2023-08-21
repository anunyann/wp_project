package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import mk.ukim.finki.akreditacii.service.StudyProgramDetailsService;
import mk.ukim.finki.akreditacii.service.StudyProgramService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("admin/study-programs")
@Controller
public class StudyProgramManagement {
    private final StudyProgramService studyProgramService;
    private final StudyProgramDetailsService studyProgramDetailsService;
    private final AccreditationService accreditationService;

    public StudyProgramManagement(StudyProgramService studyProgramService, StudyProgramDetailsService studyProgramDetailsService, AccreditationService accreditationService) {
        this.studyProgramService = studyProgramService;
        this.studyProgramDetailsService = studyProgramDetailsService;
        this.accreditationService = accreditationService;
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
        if (nameSearch == null && filteredAccreditation == null && filteredStudyCycle == null &&
                filteredDurationInYears == null && filteredOnEnglish == null) {
            studyProgramDetailsPage = studyProgramDetailsService
                    .findAllWithPagination(pageNum, results);
        } else {
            studyProgramDetailsPage = studyProgramDetailsService
                    .findAllWithPaginationFiltered(pageNum, results,
                            nameSearch, filteredAccreditation, filteredStudyCycle,
                            filteredDurationInYears, filteredOnEnglish);

            model.addAttribute("text", nameSearch);
            model.addAttribute("accreditation", filteredAccreditation);
            model.addAttribute("studyCycle", filteredStudyCycle);
            model.addAttribute("durationInYears", filteredDurationInYears);
            model.addAttribute("onEnglish", filteredOnEnglish != null);

        }
        model.addAttribute("studyProgramDetailsPage", studyProgramDetailsPage);
        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("studyCycles", StudyCycle.values());

        return "study_program/study_program_list";
    }

    @GetMapping("/add-form")
    public String addStudyProgram(Model model) {
        model.addAttribute("studyCycles", StudyCycle.values());
        model.addAttribute("accreditations", accreditationService.findAll());
        return "study_program/add_study_program.html";
    }

    @GetMapping("/edit-form/{id}")
    public String editStudyProgram(Model model, @PathVariable String id) {
        StudyProgramDetails studyProgramDetails = studyProgramDetailsService.findById(id).get();
        model.addAttribute("studyProgramDetails", studyProgramDetails);
        model.addAttribute("studyCycles", StudyCycle.values());
        model.addAttribute("accreditations", accreditationService.findAll());

        return "study_program/add_study_program.html";
    }

    @PostMapping("/add")
    public String add(Model model, @RequestParam String code, @RequestParam String name, @RequestParam String nameEn, @RequestParam String accreditationId, @RequestParam StudyCycle studyCycle, @RequestParam(required = false) Float order, @RequestParam(required = false) Short durationYears, @RequestParam(required = false) Short durationSemesters, @RequestParam(required = false) String generalInformation, @RequestParam(required = false) String graduationTitle, @RequestParam(required = false) String graduationTitleEn, @RequestParam(required = false) String subjectRestrictions, @RequestParam(required = false) String onEnglish, @RequestParam(required = false) String bilingual) {

        studyProgramDetailsService.save(code, name, nameEn, order, durationYears, durationSemesters, generalInformation, graduationTitle, graduationTitleEn, subjectRestrictions, onEnglish != null, studyCycle, accreditationService.findById(accreditationId).get(), bilingual != null);
        return "redirect:/admin/study-programs";
    }


    @GetMapping("/delete/{id}")
    public String deleteStudyProgram(@PathVariable String id) {
        this.studyProgramDetailsService.deleteById(id);
        return "redirect:/admin/study-programs";
    }
}
