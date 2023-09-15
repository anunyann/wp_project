package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.Book;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectRepository;
import mk.ukim.finki.akreditacii.service.DisplayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.function.Predicate.*;
import static java.util.stream.Collectors.groupingBy;

@Controller
public class DisplayController {

    private final DisplayService service;
    private final StudyProgramSubjectRepository programSubjectRepository;


    public DisplayController(DisplayService service, StudyProgramSubjectRepository programSubjectRepository) {
        this.service = service;
        this.programSubjectRepository = programSubjectRepository;
    }

    @GetMapping(value = {"/{accreditation}/{cycle}"})
    public String accreditationPrograms(@PathVariable String accreditation, @PathVariable StudyCycle cycle, Model model) {
        List<StudyProgramDetails> programs = service.findAccreditationProgramsByCycle(accreditation, cycle);

        model.addAttribute("acc", accreditation);
        model.addAttribute("cycle", cycle);
        model.addAttribute("programs", programs);
        return "accreditation";
    }

    @GetMapping("/sp/{program}")
    public String programSubjects(@PathVariable String program, Model model) {
        List<StudyProgramSubject> subjects = service.getProgramSubjects(program);

        Map<Short, Map<Boolean, List<StudyProgramSubject>>> bySemesterAndMandatory = subjects.stream()
                .collect(groupingBy(StudyProgramSubject::getSemester, groupingBy(StudyProgramSubject::getMandatory)));

        StudyProgramDetails studyProgramDetails = this.service.getStudyProgramDetailsById(program);
        if (!subjects.isEmpty()) {
            model.addAttribute("studyProgram", studyProgramDetails);
        }
        model.addAttribute("bySemesterAndMandatory", bySemesterAndMandatory);
        return "study_program";
    }


    @GetMapping("/program/{program}/{lang}")
    public String groupedProgramSubjects(@PathVariable String program, @PathVariable(required = false) String lang, Model model) {
        List<StudyProgramSubject> subjects = service.getProgramSubjects(program);

        Map<Short, List<StudyProgramSubject>> mandatoryBySemester = subjects.stream()
                .filter(StudyProgramSubject::getMandatory)
                .collect(groupingBy(StudyProgramSubject::getSemester));


        Map<String, List<StudyProgramSubject>> electiveByGroup = new TreeMap<>(subjects.stream()
                .filter(not(StudyProgramSubject::getMandatory))
                .collect(groupingBy(StudyProgramSubject::getSubjectGroup)));


        StudyProgramDetails studyProgramDetails = this.service.getStudyProgramDetailsById(program);
        if (!subjects.isEmpty()) {
            model.addAttribute("studyProgram", studyProgramDetails);
            if(StudyCycle.UNDERGRADUATE.equals(studyProgramDetails.getStudyCycle())) {
                model.addAttribute("back", "/mk/dodiplomski-studii");
                model.addAttribute("backTitle", "Додипломски студии");
            } else if(StudyCycle.MASTER.equals(studyProgramDetails.getStudyCycle())) {
                model.addAttribute("back", "/mk/magisterski_studii");
                model.addAttribute("backTitle", "Магистерски студии");
            } else {
                model.addAttribute("back", "/mk/doktorski_studii");
                model.addAttribute("backTitle", "Докторски студии");
            }
        }
        model.addAttribute("mandatoryBySemester", mandatoryBySemester);
        model.addAttribute("electiveByGroup", electiveByGroup);
        if ("en".equals(lang)) {
            return "study_program_grouped_en";
        } else {
            return "study_program_grouped_mk";
        }
    }

    @GetMapping("/program/{program}/edit-subjects")
    public String programSubjectManagement(@PathVariable String program, @RequestParam(defaultValue = "mk") String lang, Model model){

        List<StudyProgramSubject> subjects = service.getProgramSubjects(program);

        Map<Short, Map<Boolean, List<StudyProgramSubject>>> bySemesterAndMandatory = subjects.stream()
                .collect(groupingBy(StudyProgramSubject::getSemester, groupingBy(StudyProgramSubject::getMandatory)));

        Map<StudyProgramSubject,List<StudyProgram>> subjectStudyProgramMap = new HashMap<StudyProgramSubject,List<StudyProgram>>();

        StudyProgramDetails studyProgramDetails = this.service.getStudyProgramDetailsById(program);
        if (!subjects.isEmpty()) {
            model.addAttribute("studyProgram", studyProgramDetails);
        }
        model.addAttribute("bySemesterAndMandatory", bySemesterAndMandatory);
        return "study_program_edit";
    }

    //TODO: action for saving an edited subject
    @PostMapping("/program/{program}/edit/{code}")
    public String programEditSubject(@PathVariable String program,
                                     @PathVariable String code,
                                     @RequestParam Float credits,
                                     @RequestParam Short semester,
                                     @RequestParam StudyProgram studyProgram,
                                     @RequestParam String subjectGroup){

        return "redirect:study_program_edit";
    }

    //TODO: action for adding a subject to semester
//    @PostMapping("/program/{program}/add")
//    public String programAddSubject(@PathVariable String program, @RequestParam String code, @RequestParam Short semester, @RequestParam Boolean type){
//        List<StudyProgramSubject> subjects = service.getProgramSubjects(program);
//
//        StudyProgramSubject temp = subjects.stream().filter(i->i.getId().equals(code)).findFirst().get();
//
//
//        return "redirect:/study_program_edit";
//    }

    // Action for deleting a subject from semester (table row)
    @PostMapping("/program/{program}/delete/{code}")
    public String programSubjectDelete (@PathVariable String program, @PathVariable String code){
        List<StudyProgramSubject> subjects = service.getProgramSubjects(program);

        subjects.remove(subjects.stream().filter(i->i.getId().equals(code)).findFirst().get());
        return "redirect:/study_program_edit";
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
