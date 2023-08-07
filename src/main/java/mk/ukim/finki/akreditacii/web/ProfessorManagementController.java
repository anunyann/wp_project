package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.service.ProfessorDetailsService;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfessorManagementController {
    private final ProfessorDetailsService professorDetailsService;
    private final ProfessorService professorService;
    public ProfessorManagementController(ProfessorDetailsService professorDetailsService, ProfessorService professorService){
        this.professorDetailsService= professorDetailsService;
        this.professorService= professorService;
    }
    @GetMapping(value = {"/professor/{name}"})
    public String professorDetails(@PathVariable String name, Model model){
        model.addAttribute("professor", professorService.getProfessorById(name));

        return "professor_details";

    }
}
