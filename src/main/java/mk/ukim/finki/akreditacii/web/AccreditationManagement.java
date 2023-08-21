package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("admin/accreditations")
@Controller
public class AccreditationManagement {
    private final AccreditationService accreditationService;

    public AccreditationManagement(AccreditationService accreditationService) {
        this.accreditationService = accreditationService;
    }

    @GetMapping
    public String pageableAccreditations(Model model,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer results) {
        Page<Accreditation> accreditationsPage = accreditationService
                .findAllWithPagination(pageNum, results);
        model.addAttribute("accreditationsPage", accreditationsPage);
        return "accreditation/accreditation_list";

    }

    @GetMapping("/add-form")
    public String addAccreditation(Model model) {
        return "accreditation/add_accreditation";
    }

    @GetMapping("/edit-form/{id}")
    public String addAccreditation(Model model, @PathVariable String id) {
        Accreditation accreditation = accreditationService.findById(id).get();
        model.addAttribute("accreditation", accreditation);
        return "accreditation/add_accreditation";
    }

    @PostMapping("/add")
    public String add(Model model, @RequestParam String year,
                      @RequestParam LocalDate activeFrom,
                      @RequestParam LocalDate activeTo,
                      @RequestParam List<String> studyProgramFields) {

        accreditationService.save(year, activeFrom, activeTo, studyProgramFields);
        return "redirect:/admin/accreditations";
    }


    @GetMapping("/delete/{id}")
    public String deleteAccreditation(@PathVariable String id) {
        this.accreditationService.deleteById(id);
        return "redirect:/admin/accreditations";
    }

    @GetMapping("/activate/{id}")
    public String activateAccreditation(@PathVariable String id) {
        this.accreditationService.activate(id);
        return "redirect:/admin/accreditations";
    }

}
