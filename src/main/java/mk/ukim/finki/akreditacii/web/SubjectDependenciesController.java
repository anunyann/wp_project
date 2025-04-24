package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.subject.Subject;
import mk.ukim.finki.akreditacii.model.SubjectDependencies;
import mk.ukim.finki.akreditacii.model.SubjectDependencyType;
import mk.ukim.finki.akreditacii.service.SubjectDependenciesService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/subject")
public class SubjectDependenciesController {

    private final SubjectDependenciesService dependenciesService;
    private final SubjectService subjectService;

    public SubjectDependenciesController(SubjectDependenciesService dependenciesService,
                                         SubjectService subjectService) {
        this.dependenciesService = dependenciesService;
        this.subjectService = subjectService;
    }

    @GetMapping("/{subjectId}/edit-dependencies")
    public String editDependencies(@PathVariable String subjectId, Model model) {
        // If findById returns null when subject doesn't exist, add a null check
        Subject subject = subjectService.findById(subjectId);

        if (subject == null) {
            throw new RuntimeException("Subject not found with ID: " + subjectId);
        }

        // Get existing dependencies
        List<SubjectDependencies> dependencies = dependenciesService.findBySubjectCode(subject.getId());

        // Rest of your code remains the same
        List<SubjectDependencyType> dependencyTypes = Arrays.asList(SubjectDependencyType.values());

        model.addAttribute("subject", subject);
        model.addAttribute("dependencies", dependencies);
        model.addAttribute("dependencyTypes", dependencyTypes);
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