package mk.ukim.finki.akreditacii.web;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.repository.import_repository.ImportRepository;
import mk.ukim.finki.akreditacii.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    private final UserService userService;
    private final ImportRepository importRepository;

    public UserController(UserService userService, ImportRepository importRepository) {
        this.userService = userService;
        this.importRepository = importRepository;
    }

    @GetMapping
    public String pageableUser(Model model,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer results,
                                    @RequestParam(required = false) String searchString,
                                    @RequestParam(required = false) String roleFilter){
        Page<User> userPage;


        if (searchString == null && roleFilter == null) {
            userPage = userService
                    .findAllWithPagination(pageNum, results);
        } else {
            userPage = userService
                    .findAllWithPaginationFiltered(pageNum, results,
                            searchString, roleFilter);

            model.addAttribute("searchString", searchString);
            model.addAttribute("roleFilter", roleFilter);

        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("userRoles", UserRole.values());

        return "user/user_list";
    }

    @GetMapping(value = {"/add-user"})
    public String addProfessor(Model model) {
        model.addAttribute("userRoles", UserRole.values());
        return "user/add_user";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam String name, @RequestParam String id, @RequestParam String email, @RequestParam(required = false) UserRole role) {

        userService.save(id, name, email, role);
//        ProfessorDetails professorDetails = new ProfessorDetails(id, professorService.getProfessorById(id), 1F, degree, title.toString(), dateOfBirthParsed, null);
//        professorDetailsService.save(professorDetails);


        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/{id}/edit"})
    public String editProfessor(@PathVariable String id, Model model) {

        User user = userService.getUserById(id);
//        ProfessorDetails professorDetails = professorDetailsService.findById(id);
        model.addAttribute("user", user);
//        model.addAttribute("professorDetails", professorDetails);
        model.addAttribute("userRoles", UserRole.values());
//        model.addAttribute("educationDegrees", EducationDegree.values());

        return "user/add_user";
    }

    @GetMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable String id) {

        userService.deleteById(id);

        return "redirect:/admin/user";
    }

    @PostMapping("/import")
    public void importStudents(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<User> students = importRepository.readEnrolments(file, User.class);

        List<User> invalidEnrollments = userService.importData(students);

        String fileName = "invalid_students.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeEnrollments(User.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/export")
//    public void export(@RequestParam String name, HttpServletResponse response) {
//        String tsv = userService.toTsv(userService.list(name, null, null, null, 1, 10000).getContent());
//
//        response.setContentType("text/tab-separated-values");
//        response.setHeader("Content-Disposition", "attachment; filename=\"schedule_import.tsv\"");
//
//        try (BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
//            outputStream.write(tsv);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
