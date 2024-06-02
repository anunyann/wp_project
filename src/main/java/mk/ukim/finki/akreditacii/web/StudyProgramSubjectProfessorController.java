package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
@RequestMapping("/programs")
public class StudyProgramSubjectProfessorController {

    private final StudyProgramDetailsService studyProgramDetailsService;

    private final ProfessorService professorService;
    private final StudyProgramSubjectService studyProgramSubjectService;
    private final StudyProgramSubjectProfessorService studyProgramSubjectProfessorService;

    public StudyProgramSubjectProfessorController(StudyProgramDetailsService studyProgramDetailsService,  ProfessorService professorService, StudyProgramSubjectService studyProgramSubjectService, StudyProgramSubjectProfessorService studyProgramSubjectProfessorService) {

        this.studyProgramDetailsService = studyProgramDetailsService;
        this.professorService = professorService;
        this.studyProgramSubjectService = studyProgramSubjectService;
        this.studyProgramSubjectProfessorService = studyProgramSubjectProfessorService;
    }


    @GetMapping("/{id}/edit-professors")
    public String editProfessors(Model model, @PathVariable String id)
    {

        Optional<StudyProgramDetails> optionalStudyProgramDetails = studyProgramDetailsService.findById(id);
        if (optionalStudyProgramDetails.isPresent()) {
            StudyProgramDetails studyProgramDetails = optionalStudyProgramDetails.get();
            model.addAttribute("studyProgramDetails", studyProgramDetails);

            List<StudyProgramSubject> subjects = studyProgramSubjectService.findAllByStudyProgramCodeOrderBySemesterAscOrderAscSubjectIdAsc(studyProgramDetails.getStudyProgram().getCode());
            model.addAttribute("subjects", subjects);

            Map<StudyProgramSubject, List<StudyProgramSubjectProfessor>> subjectProfessorsMap = new TreeMap<>((subj1, subj2) -> subj1.getSubject().getId().compareTo(subj2.getSubject().getId()));
            Map<StudyProgramSubject, List<Professor>> subjectNotAssignedProfessorsMap = new HashMap<>();

            for (StudyProgramSubject studyProgramSubject : subjects) {
                List<StudyProgramSubjectProfessor> professors = studyProgramSubjectProfessorService.professorsFromStudyProgramSubject(studyProgramSubject);
                List<Professor> professorsNotAssigned = studyProgramSubjectProfessorService.professorsNotOnSubject(studyProgramSubject.getId());

                subjectNotAssignedProfessorsMap.put(studyProgramSubject, professorsNotAssigned);
                subjectProfessorsMap.put(studyProgramSubject, professors);
            }

            model.addAttribute("programCode", studyProgramDetails.getStudyProgram().getCode());
            model.addAttribute("professorsNotAssigned", subjectNotAssignedProfessorsMap);
            model.addAttribute("subjectProfessorsMap", subjectProfessorsMap);
            return "study_program_subject/edit-professors";
        }
        return null;

    }

    @Transactional
    @PostMapping("/{subjectId}/delete-professor/{professorId}")
    public String removeProfessorFromSubject(@PathVariable("subjectId") String subjectId,
                                             @PathVariable("professorId") String professorId) {

        StudyProgramSubject studyProgramSubject = studyProgramSubjectService.findById(subjectId);
        Professor professor = professorService.getProfessorById(professorId);


        studyProgramSubjectProfessorService.deleteProfessorForSubject(professor, studyProgramSubject);

        return "redirect:/programs/" + studyProgramSubject.getStudyProgram().getCode() + "/edit-professors";
    }

    @PostMapping("/{id}/add-professor")
    public String addProfessor(@PathVariable String id, @RequestParam("selectedProfessor") String selectedProfessorId,@RequestParam("order") Float order) {

        StudyProgramSubject studyProgramSubject = studyProgramSubjectService.findById(id);

        String newId = id+"-"+selectedProfessorId;
        studyProgramSubjectProfessorService.save(newId, id,selectedProfessorId,order);

        return "redirect:/programs/" + studyProgramSubject.getStudyProgram().getCode() + "/edit-professors";
    }

    @PostMapping("/{subjectId}/add-professors-from-other-study-programs")
    public String addProfessorsFromOtherStudyPrograms(@PathVariable("subjectId") String subjectId,@RequestParam("order") Float order) {

        StudyProgramSubject studyProgramSubject = studyProgramSubjectService.findById(subjectId);
        List<StudyProgramSubjectProfessor> professorsAssignedToTheSubject = studyProgramSubjectProfessorService.professorsFromStudyProgramSubject(studyProgramSubject);
        List<Professor> assignedProfessors = professorsAssignedToTheSubject.stream().map(StudyProgramSubjectProfessor::getProfessor).toList();

        List<Professor> professorsFromOtherStudyProgramsForTheSubject = studyProgramSubjectProfessorService.findProfessorsFromOtherProgramsForSubject(studyProgramSubject.getSubject().getId(),studyProgramSubject.getStudyProgram().getCode());

        List<Professor> professors = professorsFromOtherStudyProgramsForTheSubject.stream().filter(p -> !assignedProfessors.contains(p)).toList();

        for(Professor professor: professors)
        {
            String newId = subjectId + "-" + professor.getId();
            studyProgramSubjectProfessorService.save(newId, subjectId, professor.getId(), order);
        }

        return "redirect:/programs/" + studyProgramSubject.getStudyProgram().getCode() + "/edit-professors";
    }

    @PostMapping("/{programCode}/add-all-professors")
    String addAllProfessorsToAllSubjectsFromTheStudyProgram(@PathVariable("programCode") String programCode,@RequestParam("order") Float order)
    {

        List<Professor> assignedProfessors = new ArrayList<>();

        List<StudyProgramSubject> studyProgramSubjects = studyProgramSubjectService.findAllByStudyProgram(programCode);

        for (StudyProgramSubject s : studyProgramSubjects) {

            List<StudyProgramSubjectProfessor> professorsAssignedToTheSubject =
                    studyProgramSubjectProfessorService.professorsFromStudyProgramSubject(s);

            List<Professor> professors = professorsAssignedToTheSubject.stream()
                    .map(StudyProgramSubjectProfessor::getProfessor)
                    .toList();

            assignedProfessors.addAll(professors);
            List<Professor> professorsFromOtherStudyProgramsForTheSubject =
                    studyProgramSubjectProfessorService.findProfessorsFromOtherProgramsForSubject(
                            s.getSubject().getId(), s.getStudyProgram().getCode());

            List<Professor> uniqueProfessors = professorsFromOtherStudyProgramsForTheSubject.stream()
                    .filter(p -> !assignedProfessors.contains(p))
                    .toList();

            for (Professor professor : uniqueProfessors) {
                String newId = s.getId() + "-" + professor.getId();
                studyProgramSubjectProfessorService.save(newId, s.getId(), professor.getId(), order);
            }
        }
        return "redirect:/programs/" + programCode + "/edit-professors";
    }



}
