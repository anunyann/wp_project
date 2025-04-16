package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserDto;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.repository.UserRepository;
import mk.ukim.finki.akreditacii.service.UserService;
import mk.ukim.finki.akreditacii.service.specifications.FieldFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    public Page<User> findAllWithPaginationAndFilters(Integer pageNum,
                                                      Integer results,
                                                      String id,
                                                      String email,
                                                      String name,
                                                      String role) {

        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        Specification<User> spec = Specification.where(null);
        if (id != null) {
            System.out.println("Ulava ID e: " + id);
            spec = spec.and(FieldFilterSpecification.filterEquals(User.class, "id", id));
        }
        if (email != null) {
            spec = spec.and(FieldFilterSpecification.filterEquals(User.class, "email", email));
        }
        if (name != null) {
            spec = spec.and(FieldFilterSpecification.filterContainsText(User.class, "name", name));
        }
        if (role != null) {
            try {
                UserRole userRole = UserRole.valueOf(role); // Convert String to Enum
                spec = spec.and(FieldFilterSpecification.filterEqualsV(User.class, "role", userRole));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role value: " + role);

            }
        }

        return userRepository.findAll(spec, pageRequest);
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


    public void processGroup (StringBuilder sb, User user) {
        sb.append(user.getId()).append("\t");
        sb.append(user.getName()).append("\t");
        sb.append(user.getEmail()).append("\t");
        sb.append(user.getRole().roleName()).append("\t");
        sb.append("\n");
    }

}
