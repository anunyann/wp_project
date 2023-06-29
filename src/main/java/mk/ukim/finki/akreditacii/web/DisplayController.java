package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.Book;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.service.DisplayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DisplayController {

    private final DisplayService service;


    public DisplayController(DisplayService service) {
        this.service = service;
    }

    @GetMapping("/subject/{subjectId}")
    public String subjectDetails(@PathVariable String subjectId, Model model) {
        SubjectDetails sd = service.getSubjectDetailsById(subjectId);
        List<StudyProgramSubject> subjectStudyPrograms = service.getSubjectPrograms(subjectId);
        List<Professor> subjectProfessors = service.getSubjectProfessors(subjectId);
        List<Book> mandatoryBooks = sd.getBibliography().getBooks();
        List<Book> additionalBooks = sd.getBibliography().getElectiveBooks();


        model.addAttribute("sd", sd);
        model.addAttribute("subjectStudyPrograms", subjectStudyPrograms);
        model.addAttribute("subjectProfessors", subjectProfessors);
        model.addAttribute("mandatoryBooks", mandatoryBooks);
        model.addAttribute("additionalBooks", additionalBooks);
        return "subject";

    }
}
