package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {

    Page<User> findAllStaff(Pageable pageable);

    Page<User> findStaffByRole(UserRole role, Pageable pageable);

    User findById(String id);
}
