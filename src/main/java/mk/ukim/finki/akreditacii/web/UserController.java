package mk.ukim.finki.akreditacii.web;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserDto;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.repository.ImportRepository;
import mk.ukim.finki.akreditacii.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String id,
                               @RequestParam(required = false) String role) {
        Page<User> userPage;

        if (name == null || id == null || email == null || role == null) {
            userPage = userService.findAllWithPagination(pageNum, results);
        } else {
            userPage = userService.findAllWithPaginationAndFilters(pageNum,results,name,email,id,role);

            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("id", id);

        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userRoles", UserRole.values());

        return "user/list";

    }


    @GetMapping(value = {"/add-user"})
    public String addProfessor(Model model) {
        model.addAttribute("userRoles", UserRole.values());
        return "user/add_user";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam String name, @RequestParam String id, @RequestParam String email, @RequestParam(required = false) UserRole role) {
        userService.save(id, name, email, role);
        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/{id}/edit"})
    public String editProfessor(@PathVariable String id, Model model) {

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("userRoles", UserRole.values());
        return "user/add_user";
    }

    @GetMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/admin/user";
    }

    @PostMapping("/import")
    public void importUsers(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        List<UserDto> users = importRepository.readFile(file, UserDto.class);

        List<UserDto> invalidEnrollments = userService.importStudents(users);

        String fileName = "invalid_users.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeToOutputStream(UserDto.class, invalidEnrollments, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<UserDto> example = new ArrayList<>();
        example.add(new UserDto("test4.test4", "Test4", "test4@test4.com", UserRole.STUDENT));

        String fileName = "users_example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeToOutputStream(UserDto.class, example, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
