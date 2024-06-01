package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserDto;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getUserById(String id);

    Page<User> findAllWithPagination(int pageNum, int pageSize);

    Page<User> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredRole);

    User save(String id, String name, String email, UserRole role);

    void deleteById(String id);

    List<User> findAll();

    //List<User> importData(List<User> users);
    List<UserDto> importStudents(List<UserDto> students);

    //String toTsv(List<User> users);
    //Page<User> list(String id, String name, String email, String role);

}
