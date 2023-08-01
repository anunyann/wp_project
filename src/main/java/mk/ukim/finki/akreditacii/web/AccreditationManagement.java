package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("accreditations")
@Controller
public class AccreditationManagement {
    private final AccreditationService accreditationService;

    public AccreditationManagement(AccreditationService accreditationService) {
        this.accreditationService = accreditationService;
    }

    @GetMapping
    public String allAccreditations(Model model) {
        List<Accreditation> accreditations = accreditationService.findAll();
        model.addAttribute("accreditations", accreditations);
        return "accreditation_list";
    }

    @GetMapping("/add-form")
    public String addAccreditation(Model model) {
        return "add_accreditation";
    }

    @GetMapping("/edit-form/{id}")
    public String addAccreditation(Model model, @PathVariable String id) {
        Accreditation accreditation = accreditationService.findById(id).orElseThrow(() -> new NoSuchElementException());
        model.addAttribute("accreditation", accreditation);
        return "add_accreditation";
    }

    @PostMapping("/add")
    public String add(Model model, @RequestParam String year,
                      @RequestParam LocalDate activeFrom,
                      @RequestParam LocalDate activeTo,
                      @RequestParam List<String> studyProgramFields) {

        accreditationService.save(year, activeFrom, activeTo, studyProgramFields);
        return "redirect:/accreditations";
    }


    @GetMapping("/delete/{id}")
    public String deleteAccreditation(@PathVariable String id) {
        this.accreditationService.deleteById(id);
        return "redirect:/accreditations";
    }

    @GetMapping("/activate/{id}")
    public String activateAccreditation(@PathVariable String id) {
        this.accreditationService.activate(id);
        return "redirect:/accreditations";
    }

}
