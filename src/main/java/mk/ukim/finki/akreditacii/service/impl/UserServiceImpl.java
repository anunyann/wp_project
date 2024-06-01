package mk.ukim.finki.akreditacii.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserDto;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.repository.UserRepository;
import mk.ukim.finki.akreditacii.repository.import_repository.ImportRepository;
import mk.ukim.finki.akreditacii.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new InvalidId(id));
    }

    @Override
    public Page<User> findAllWithPagination(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Page<User> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredRole) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        return userRepository.findAllFiltered(stringSearch,
                !filteredRole.equals("") ? UserRole.valueOf(filteredRole) : null,
                pageRequest);
    }

    @Override
    public User save(String id, String name, String email, UserRole role) {
        User user = new User(id,name,email,role);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDto> importStudents(List<UserDto> students) {
        return students.stream()
                .map(dto -> saveUser(dto))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    private Optional<UserDto> saveUser(UserDto dto) {
        try {
            User user = new User(dto.getId(),
                    dto.getName(),
                    dto.getEmail(),
                    dto.getRole()
            );
            this.userRepository.save(user);
            return Optional.empty();
        } catch (Exception e) {
            dto.setMessage(e.getMessage());
        }
        return Optional.of(dto);
    }



//    @Override
//    public String toTsv(List<User> users) {
//        StringBuilder sb = new StringBuilder();
//        users.forEach(user -> {
//            processGroup(sb, user);
//        });
//        return sb.toString();
//    }

//    @Override
//    public Page<User> list(String id, String name, String email, String role) {
//        return null;
//    }

//    @Override
//    public Page<User> list(String id, String name, String email, String role) {
//        return null;
//    }

//    @Override
//    public Page<User> list(String id, String name, String email, String role) {
//        Page<User> page = userService.list(id, name, email, role);
//        doExport(response, page.getContent().stream()
//                .map(it -> new UserDto(it,
//                        coursePreferenceRepository.findById(it.getJoinedSubject().getAbbreviation()).orElse(null))
//                )
//                .collect(Collectors.toList()));
//
//    }

//    public void doExport(HttpServletResponse response, List<UserDto> data) {
//        String fileName = "courses.tsv";
//        response.setContentType("text/tab-separated-values");
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//
//        try (OutputStream outputStream = response.getOutputStream()) {
//            importRepository.writeEnrollments(UserDto.class, data, outputStream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public void processGroup (StringBuilder sb, User user) {
        sb.append(user.getId()).append("\t");
        sb.append(user.getName()).append("\t");
        sb.append(user.getEmail()).append("\t");
        sb.append(user.getRole().roleName()).append("\t");
        sb.append("\n");
    }


}
