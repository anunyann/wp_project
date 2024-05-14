package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.Subject;
import mk.ukim.finki.akreditacii.service.StudyProgramDetailsService;
import mk.ukim.finki.akreditacii.service.StudyProgramSubjectService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("admin/study-programs")
@Controller
public class StudyProgramSubjectManagement {

    private final StudyProgramDetailsService studyProgramDetailsService;
    private final StudyProgramSubjectService studyProgramSubjectService;
    private final SubjectService subjectService;

    public StudyProgramSubjectManagement(StudyProgramDetailsService studyProgramDetailsService, StudyProgramSubjectService studyProgramSubjectService, SubjectService subjectService) {
        this.studyProgramDetailsService = studyProgramDetailsService;
        this.studyProgramSubjectService = studyProgramSubjectService;
        this.subjectService = subjectService;
    }

    @GetMapping("/{id}/edit-subjects")
    public String editSubjects(Model model, @PathVariable String id) {
        StudyProgramDetails studyProgramDetails = studyProgramDetailsService.findById(id).get();
        List<StudyProgramSubject> subjects = studyProgramSubjectService.findByStudyProgram(studyProgramDetails);

        model.addAttribute("studyProgramDetails", studyProgramDetails);
        model.addAttribute("subjects", subjects);

        return "study_program_subject/study_program_edit_subjects";
    }
    @PostMapping("/{id}/edit-subjects")
    public String saveEditedSubject(@PathVariable String id, @RequestParam String subjectId, @RequestParam String name, @RequestParam boolean mandatory, @RequestParam  short semester, RedirectAttributes redirectAttributes) {

        this.studyProgramSubjectService.edit(subjectId, name, mandatory, semester);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/admin/study-programs/" + id + "/edit-subjects";
    }

    @GetMapping("/{id}/remove-subject/{subjectId}")
    public String removeSubject(@PathVariable String id, @PathVariable String subjectId, RedirectAttributes redirectAttributes){
        try{
            boolean hasAssociatedProfessors = studyProgramSubjectService.hasAssociatedProfessors(subjectId);
            if(hasAssociatedProfessors){
                redirectAttributes.addFlashAttribute("error", "Cannot delete the subject because associated professors exist. Remove the professors from the subject first.");
            } else {
                studyProgramSubjectService.remove(subjectId);
                redirectAttributes.addFlashAttribute("success", "Subject removed successfully.");
            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while removing the subject.");
        }
        return "redirect:/admin/study-programs/" + id + "/edit-subjects";
    }

    @GetMapping("{id}/add-subject")
    public String addSubjectForm(Model model, @PathVariable String id){
        StudyProgramDetails studyProgramDetails = studyProgramDetailsService.findById(id).orElse(null);
        List<Subject> subjects = subjectService.findAllSubjects();

        model.addAttribute("studyProgramDetails", studyProgramDetails);
        model.addAttribute("subjects", subjects);


        return "study_program_subject/add_study_program_subject";
    }

    @PostMapping("{id}/add-subject")
    public String addSubject(@PathVariable String id, @RequestParam String subjectId, @RequestParam boolean mandatory,  @RequestParam  short semester, @RequestParam float order, RedirectAttributes redirectAttributes) {
        StudyProgramDetails studyProgramDetails = studyProgramDetailsService.findById(id).orElse(null);
        if (studyProgramDetails != null) {

            this.studyProgramSubjectService.add(id, subjectId, mandatory, semester, order);

            redirectAttributes.addAttribute("id", id);
            return "redirect:/admin/study-programs/" + id + "/edit-subjects";
        }
        return "redirect:/admin/study-programs/" + id + "/edit-subjects";
    }

}
