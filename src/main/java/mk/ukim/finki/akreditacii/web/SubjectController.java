package mk.ukim.finki.akreditacii.web;


import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.Book;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import mk.ukim.finki.akreditacii.service.DisplayService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SubjectController {

    private final SubjectService service;
    private final AccreditationService accreditationService;

    private final DisplayService displayService;

    public SubjectController(SubjectService service, AccreditationService accreditationService, DisplayService displayService) {
        this.service = service;
        this.accreditationService = accreditationService;
        this.displayService = displayService;
    }


    @GetMapping("/subject/list")
    public String findAllSubjectsFiltered(Model model,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer results,
                                                   @RequestParam(required = false) String nameSearch,
                                                   @RequestParam(required = false) String filteredAccreditation
                                                   ){
        Page<SubjectDetails> subjectDetailsListPage;

        if (nameSearch == null && filteredAccreditation == null) {
            subjectDetailsListPage = service.findAllWithPagination(pageNum,results);

        } else {
            subjectDetailsListPage =  service.findAllWithPaginationFiltered(pageNum,results,nameSearch,filteredAccreditation);
            model.addAttribute("text", nameSearch);
            model.addAttribute("accreditation", filteredAccreditation);
        }

        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("subjectDetailsListPage",subjectDetailsListPage);
        return "subject/subject_list.html";
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
        return "subject/edit_subject";
    }

    @GetMapping("/subject/{subjectId}/edit/mandatory-books")
    public String getMandatoryBooks(@PathVariable String subjectId, Model model) {
        SubjectDetails sd = service.getSubjectDetailsById(subjectId);
        List<Book> mandatoryBooks = sd.getBibliography().getBooks();
        Book newBook = new Book();

        model.addAttribute("newBook",newBook);
        model.addAttribute("subjectId", sd.getId());
        model.addAttribute("books", mandatoryBooks);
        return "subject/edit_mandatory_books";
    }

    @GetMapping("/subject/{subjectId}/edit/elective-books")
    public String getAdditionalBooks(@PathVariable String subjectId, Model model) {
        SubjectDetails sd = service.getSubjectDetailsById(subjectId);
        List<Book> additionalBooks = sd.getBibliography().getElectiveBooks();
        Book newBook = new Book();

        model.addAttribute("subjectId", sd.getId());
        model.addAttribute("books", additionalBooks);
        model.addAttribute("newBook",newBook);
        return "subject/edit_elective_books";
    }

    @PostMapping("/remove-book/{subjectId}/{bookId}")
    public String removeBook(@PathVariable("subjectId") String subjectId,
                             @PathVariable("bookId") String bookId) {



        SubjectDetails sd = service.findSubjectById(subjectId).get();
        sd.getBibliography().getBooks().remove(Integer.parseInt(bookId) -1 );
        service.updateSubject(sd);
        return "redirect:/admin/subject/list";
    }

    @PostMapping("/add-new-book/{subjectId}")
    public String addBook(@ModelAttribute("newBook") Book newBook,
                          @PathVariable("subjectId") String subjectId) {

       SubjectDetails sd = service.findSubjectById(subjectId).get();
       sd.getBibliography().getBooks().add(newBook);
       service.updateSubject(sd);
       return "redirect:/admin/subject/list";
    }

    @PostMapping("/add-new-elective-book/{subjectId}")
    public String addElectiveBook(@ModelAttribute("newBook") Book newBook,
                          @PathVariable("subjectId") String subjectId) {

        SubjectDetails sd = service.findSubjectById(subjectId).get();
        sd.getBibliography().getElectiveBooks().add(newBook);
        service.updateSubject(sd);
        return "redirect:/admin/subject/list";
    }

    @PostMapping("/update-subject")
    public String updateSubject(@ModelAttribute SubjectDetails subjectDetails) {
        this.service.updateSubject(subjectDetails);
        return "redirect:/admin/subject/list";
    }

}
