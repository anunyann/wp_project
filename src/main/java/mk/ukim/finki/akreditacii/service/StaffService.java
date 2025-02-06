package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserProfessorView;
import mk.ukim.finki.akreditacii.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {

    Page<UserProfessorView> findAllStaff(Pageable pageable);

    Page<UserProfessorView> findStaffByRole(UserRole role, Pageable pageable);

    User findById(String id);
}
