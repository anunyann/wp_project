package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.repository.UserRepository;
import mk.ukim.finki.akreditacii.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    public List<User> importData(List<User> users) {
//        return users.stream()
//                .map(us -> {
//                    try {
//                        us.setEmail(us.getEmail());
//                        us.setId(us.getId());
//                        us.setName(us.getName());
//                        us.setRole(us.getRole());
//                        userRepository.save(us);
//                        return null;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return us;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public String toTsv(List<User> users) {
        StringBuilder sb = new StringBuilder();
        users.forEach(user -> {
            processGroup(sb, user);
        });
        return sb.toString();
    }

    @Override
    public Page<User> list(String name, String email, String role) {
        return null;
    }

    public void processGroup (StringBuilder sb, User user) {
        sb.append(user.getName()).append("\t");
        sb.append(user.getEmail()).append("\t");
        sb.append(user.getRole().roleName()).append("\t");
        sb.append("\n");
    }


}
