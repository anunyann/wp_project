package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.exceptions.UserNotFoundException;
import mk.ukim.finki.akreditacii.repository.UserRepository;
import mk.ukim.finki.akreditacii.service.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;


@Service
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;


    public StaffServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAllStaff(Pageable pageable) {
        EnumSet<UserRole> excludedRoles = EnumSet.of(UserRole.STUDENT, UserRole.EXTERNAL);
        return userRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.not(root.get("role").in(excludedRoles)), pageable);
    }

    @Override
    public Page<User> findStaffByRole(UserRole role, Pageable pageable) {
        return userRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role), pageable);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }
}
