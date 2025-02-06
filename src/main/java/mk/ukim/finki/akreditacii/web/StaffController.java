package mk.ukim.finki.akreditacii.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserProfessorView;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.consultations.Consultation;
import mk.ukim.finki.akreditacii.model.consultations.ConsultationType;
import mk.ukim.finki.akreditacii.model.professor.*;
import mk.ukim.finki.akreditacii.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@AllArgsConstructor
@RequestMapping("/staff")
public class StaffController {
    private final StaffService staffService;
    private final ProfessorService professorService;
    private final ProfessorAcademicTitlesService professorAcademicTitlesService;
    private final ProfessorDetailsService professorDetailsService;
    private final ProfessorEducationService professorEducationService;
    private final ConsultationService consultationService;
    private final ProfessorResumeService professorResumeService;


    @GetMapping
    public String listStaff(@RequestParam(defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "20") int results,
                            @RequestParam(required = false) UserRole role,
                            Model model) {
        Pageable pageable = PageRequest.of(pageNum - 1, results);
        Page<UserProfessorView> staffPage;

        if (role != null) {
            staffPage = staffService.findStaffByRole(role, pageable);
        } else {
            staffPage = staffService.findAllStaff(pageable);
        }

        List<UserRole> roles = Arrays.asList(
                UserRole.PROFESSOR,
                UserRole.ACADEMIC_AFFAIR_VICE_DEAN,
                UserRole.SCIENCE_AND_COOPERATION_VICE_DEAN,
                UserRole.FINANCES_VICE_DEAN,
                UserRole.DEAN,
                UserRole.STUDENT_ADMINISTRATION,
                UserRole.STUDENT_ADMINISTRATION_MANAGER,
                UserRole.FINANCE_ADMINISTRATION,
                UserRole.FINANCE_ADMINISTRATION_MANAGER,
                UserRole.LEGAL_ADMINISTRATION,
                UserRole.ARCHIVE_ADMINISTRATION,
                UserRole.ADMINISTRATION_MANAGER
        );

        Map<UserRole, String> roleNames = Stream.of(new Object[][]{
                {UserRole.PROFESSOR, "Професори"},
                {UserRole.ACADEMIC_AFFAIR_VICE_DEAN, "Продекан за настава"},
                {UserRole.SCIENCE_AND_COOPERATION_VICE_DEAN, "Продекан за наука и соработка"},
                {UserRole.FINANCES_VICE_DEAN, "Продекан за финансии"},
                {UserRole.DEAN, "Декан"},
                {UserRole.STUDENT_ADMINISTRATION, "Студентска администрација"},
                {UserRole.STUDENT_ADMINISTRATION_MANAGER, "Раководител на студентска администрација"},
                {UserRole.FINANCE_ADMINISTRATION, "Финансиска администрација"},
                {UserRole.FINANCE_ADMINISTRATION_MANAGER, "Раководител на финансиска администрација"},
                {UserRole.LEGAL_ADMINISTRATION, "Правна администрација"},
                {UserRole.ARCHIVE_ADMINISTRATION, "Архивска администрација"},
                {UserRole.ADMINISTRATION_MANAGER, "Раководител на администрација"}
        }).collect(Collectors.toMap(data -> (UserRole) data[0], data -> (String) data[1]));


        model.addAttribute("roles", roles);
        model.addAttribute("roleNames", roleNames);
        model.addAttribute("staffPage", staffPage);
        return "staff/list";
    }

    @GetMapping("/details/{id}")
    public String staffDetails(@PathVariable String id, Model model) {
        UserProfessorView staff = staffService.findById(id);
        model.addAttribute("staff", staff);

        boolean isProfessor = staff.getRole().isProfessor();
        model.addAttribute("isProfessor", isProfessor);
        List<Consultation> regularConsultations = consultationService.listNextWeekConsultationsByProfessor(id, ConsultationType.WEEKLY);
        List<Consultation> irregularConsultations = consultationService.listNextWeekConsultationsByProfessor(id, ConsultationType.ONE_TIME);
        model.addAttribute("regularConsultations", regularConsultations);
        model.addAttribute("irregularConsultations", irregularConsultations);

        if (isProfessor) {
            Professor professor = professorService.getProfessorById(id);
            model.addAttribute("professor", professor);


            ProfessorDetails professorDetails = professorDetailsService.getByProfessorId(id);
            model.addAttribute("professorDetails", professorDetails);

            ProfessorResume resume = professorResumeService.getByProfessorId(id);
            model.addAttribute("resume", resume);


            ProfessorAcademicTitles academicTitles = professorAcademicTitlesService.findByProfessor(professor);
            model.addAttribute("academicTitles", academicTitles);

            List<ProfessorEducation> professorEducation = professorEducationService.listEducationByProfessor(professor);
            model.addAttribute("professorEducation", professorEducation);
        }

        return "staff/details";
    }
}
