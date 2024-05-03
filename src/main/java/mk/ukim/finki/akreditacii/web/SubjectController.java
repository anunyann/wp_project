package mk.ukim.finki.akreditacii.web;


import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.subject.*;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import mk.ukim.finki.akreditacii.service.DisplayService;
import mk.ukim.finki.akreditacii.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    ) {
        Page<SubjectDetails> subjectDetailsListPage;

        if (nameSearch == null && filteredAccreditation == null) {
            subjectDetailsListPage = service.findAllWithPagination(pageNum, results);

        } else {
            subjectDetailsListPage = service.findAllWithPaginationFiltered(pageNum, results, nameSearch, filteredAccreditation);
            model.addAttribute("text", nameSearch);
            model.addAttribute("accreditation", filteredAccreditation);
        }

        model.addAttribute("accreditations", accreditationService.findAll());
        model.addAttribute("subjectDetailsListPage", subjectDetailsListPage);
        return "subject/subject_list.html";
    }

    @GetMapping("/subject/{subjectId}/details")
    public String subjectDetails(@PathVariable String subjectId,
                                 Model model) {
        SubjectDetails subjectDetails = this.service.getSubjectDetailsById(subjectId);
        Subject subject = subjectDetails.getSubject();

        // All attr from Subject
        model.addAttribute("subject", subject);

        // All attr from SubjectDetails
        model.addAttribute("subjectDetails", subjectDetails);

        // All unique professors at the subject (regardless of the study program) StudyProgramSubjectProfessor, comma separated list of professor ids
        model.addAttribute("professors", this.service.getSubjectProfessors(subjectId));

        // Number of proffesors on the subject
        model.addAttribute("numProfessors", this.service.getSubjectProfessors(subjectId).size());

        // Study programs where it is mandatory StudyProgramSubject, comma separated list of study program codes
        model.addAttribute("studyPrograms", "this.service.getStudyProgramsWhereSubjectIsMandatorySeparatedWithComma(subjectId)");
        model.addAttribute("studyProgramsNum", this.service.getStudyProgramsWhereSubjectIsMandatory(subjectId).size());

        // Number of years that this subject has been activated
        model.addAttribute("yearsActive", this.service.numberOfActiveYears(subjectId));

        // Get all books for given subject
        model.addAttribute("books", subjectDetails.getBibliography().getBooks());
        model.addAttribute("numBooks", subjectDetails.getBibliography().getBooks().size());

        // Get all electiveBooks for given subject
        model.addAttribute("electiveBooks", subjectDetails.getBibliography().getElectiveBooks());

        return "subject/subject_details.html";
    }

    @GetMapping("/subject/statistics")
    public String subjectStatisctics(Model model,
                                     @RequestParam(required = false) String subjectCode,
                                     @RequestParam(required = false) String professorCode,
                                     @RequestParam(required = false) String studyProgramCode,
                                     @RequestParam(required = false) String accreditationYear) {
        //subjectCode, professorCode, studyProgramCode, accreditationYear

        if (subjectCode == null && professorCode == null && studyProgramCode == null && accreditationYear == null) {
            model.addAttribute("emptyMessage", true);
            return "subject/subject_statistics.html";
        }

        model.addAttribute("subjectCode", subjectCode);
        model.addAttribute("professorCode", professorCode);
        model.addAttribute("studyProgramCode", studyProgramCode);
        model.addAttribute("accreditationYear", accreditationYear);
        model.addAttribute("subjects", this.service.findSubjectsInfo(subjectCode, professorCode, studyProgramCode, accreditationYear));
        return "subject/subject_statistics.html";
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

        model.addAttribute("newBook", newBook);
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
        model.addAttribute("newBook", newBook);
        return "subject/edit_elective_books";
    }

    @PostMapping("/remove-book/{subjectId}/{bookId}")
    public String removeBook(@PathVariable("subjectId") String subjectId,
                             @PathVariable("bookId") String bookId) {


        SubjectDetails sd = service.findSubjectById(subjectId).get();
        sd.getBibliography().getBooks().remove(Integer.parseInt(bookId) - 1);
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
