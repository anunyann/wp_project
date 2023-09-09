package mk.ukim.finki.akreditacii.web;


import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.Book;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetailsDTO;
import mk.ukim.finki.akreditacii.service.DisplayService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectController {

    private final SubjectService service;

    private final DisplayService displayService;

    public SubjectController(SubjectService service, DisplayService displayService) {
        this.service = service;
        this.displayService = displayService;
    }

    @PostMapping("/update-subject")
    public String updateSubject(@ModelAttribute SubjectDetails subjectDetails) {
            this.service.updateSubject(subjectDetails);
            return "subject_list";
    }

    @GetMapping("/subject/list")
    public String findAll(Model model){

        List<SubjectDetails> subjectDetailsList = service.findAll();
        List<SubjectDetailsDTO> dtoList = new ArrayList<>();
        subjectDetailsList.forEach(it -> {
            SubjectDetailsDTO dto = new SubjectDetailsDTO();
            List<StudyProgramSubject> subjectStudyPrograms = service.getSubjectPrograms(it.getId());
            dto.setStudyPrograms(subjectStudyPrograms);
            dto.setCode(it.getId());
            dto.setName(it.getSubject().getName());
            dto.setNumberOfContents(it.getBibliography().getBooks().size());
            dtoList.add(dto);
        });

        model.addAttribute("subjectDetailsList",dtoList);
        return "subject_list";
    }

    @GetMapping("/subject/{subjectId}/edit")
    public String editSubject(@PathVariable String subjectId, Model model) {
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
        return "edit_subject";
    }

    @GetMapping("/subject/{subjectId}/edit/mandatory-books")
    public String getMandatoryBooks(@PathVariable String subjectId, Model model) {
        SubjectDetails sd = service.getSubjectDetailsById(subjectId);
        List<Book> mandatoryBooks = sd.getBibliography().getBooks();
        List<Book> additionalBooks = sd.getBibliography().getElectiveBooks();
        Book newBook = new Book();

        model.addAttribute("newBook",newBook);
        model.addAttribute("sd", sd);
        model.addAttribute("books", mandatoryBooks);
        return "edit_books";
    }

    @GetMapping("/subject/{subjectId}/edit/elective-books")
    public String getAdditionalBooks(@PathVariable String subjectId, Model model) {
        SubjectDetails sd = service.getSubjectDetailsById(subjectId);
        List<Book> additionalBooks = sd.getBibliography().getElectiveBooks();
        Book newBook = new Book();

        model.addAttribute("sd", sd);
        model.addAttribute("books", additionalBooks);
        model.addAttribute("newBook",newBook);
        return "edit_books";
    }



    @PostMapping("/remove-book")
    public String removeBook(@RequestParam("bookId") Long bookId) {
        // Implement logic to remove the book from the list
        // You may use a service method to handle the removal
        //bookService.removeBookById(bookId);
        return "redirect:/edit"; // Redirect back to the edit page
    }

}
