package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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
    public String pageableAccreditations(@RequestParam(name = "page", defaultValue = "1") int page,
                                         @RequestParam(name = "size", defaultValue = "4") int size,
                                         Model model) {
        Page<Accreditation> accreditationsPage = accreditationService
                .findAllWithPagination(PageRequest.of(page - 1, size));
        model.addAttribute("accreditationsPage", accreditationsPage);

        int totalPages = accreditationsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "accreditation/accreditation_list";

    }

    @GetMapping("/add-form")
    public String addAccreditation(Model model) {
        return "accreditation/add_accreditation";
    }

    @GetMapping("/edit-form/{id}")
    public String addAccreditation(Model model, @PathVariable String id) {
        Accreditation accreditation = accreditationService.findById(id).orElseThrow(() -> new NoSuchElementException());
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
