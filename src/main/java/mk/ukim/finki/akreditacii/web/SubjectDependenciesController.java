package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.Subject;
import mk.ukim.finki.akreditacii.model.SubjectDependencies;
import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.service.StudyProgramService;
import mk.ukim.finki.akreditacii.service.SubjectDependenciesService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/subject")
public class SubjectDependenciesController {

    private final SubjectDependenciesService dependenciesService;
    private final SubjectService subjectService;
    private final StudyProgramService studyProgramService;

    public SubjectDependenciesController(SubjectDependenciesService dependenciesService,
                                         SubjectService subjectService, StudyProgramService studyProgramService) {
        this.dependenciesService = dependenciesService;
        this.subjectService = subjectService;
        this.studyProgramService = studyProgramService;
    }

    @GetMapping("/{subjectId}/edit-dependencies")
    public String editDependencies(@PathVariable String subjectId, Model model) {
        // Get subject by ID
        Subject subject = subjectService.findById(subjectId);

        if (subject == null) {
            throw new RuntimeException("Subject not found with ID: " + subjectId);
        }

        // Get existing dependencies
        List<SubjectDependencies> dependencies = dependenciesService.findBySubjectCode(subject.getId());

        // Get study programs where this subject is included
        List<String> studyProgramCodes = dependenciesService.getStudyProgramsForSubject(subject.getId());

        // Get all study programs for the dropdown
        List<StudyProgram> allStudyPrograms = studyProgramService.findAll();

        // Create a filtered list of study programs that include this subject
        List<StudyProgram> subjectStudyPrograms = allStudyPrograms.stream()
                .filter(program -> studyProgramCodes.contains(program.getCode()))
                .collect(Collectors.toList());

        List<SubjectDependencyType> dependencyTypes = Arrays.asList(SubjectDependencyType.values());

        model.addAttribute("subject", subject);
        model.addAttribute("dependencies", dependencies);
        model.addAttribute("dependencyTypes", dependencyTypes);
        model.addAttribute("studyPrograms", allStudyPrograms); // All programs for the dropdown
        model.addAttribute("subjectStudyPrograms", subjectStudyPrograms); // Programs that include this subject
        model.addAttribute("newDependency", new SubjectDependencies());

        return "subject/edit-dependencies";
    }

    @PostMapping("/{subjectId}/add-dependency")
    public String addDependency(
            @PathVariable String subjectId,
            @RequestParam SubjectDependencyType dependencyType,
            @RequestParam String dependency,
            @RequestParam(required = false) String studyProgramCode,
            RedirectAttributes redirectAttributes) {

        try {
            dependenciesService.save(subjectId, studyProgramCode, dependencyType, dependency);
            redirectAttributes.addFlashAttribute("success", "Dependency added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/subject/" + subjectId + "/edit-dependencies";
    }

    @PostMapping("/{subjectId}/update-dependency/{dependencyId}")
    public String updateDependency(
            @PathVariable String subjectId,
            @PathVariable Long dependencyId,
            @RequestParam SubjectDependencyType dependencyType,
            @RequestParam String dependency,
            @RequestParam(required = false) String studyProgramCode,
            RedirectAttributes redirectAttributes) {

        try {
            dependenciesService.update(dependencyId, subjectId, studyProgramCode, dependencyType, dependency);
            redirectAttributes.addFlashAttribute("success", "Dependency updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/subject/" + subjectId + "/edit-dependencies";
    }

    @GetMapping("/{subjectId}/delete-dependency/{dependencyId}")
    public String deleteDependency(
            @PathVariable String subjectId,
            @PathVariable Long dependencyId,
            RedirectAttributes redirectAttributes) {

        dependenciesService.deleteById(dependencyId);
        redirectAttributes.addFlashAttribute("success", "Dependency deleted successfully");

        return "redirect:/admin/subject/" + subjectId + "/edit-dependencies";
    }
}