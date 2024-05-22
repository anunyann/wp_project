package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.User;
import mk.ukim.finki.akreditacii.model.UserRole;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaSpecificationRepository<User, String> {

    @Query("SELECT u FROM User u " + "WHERE (:role IS NULL OR u.role = :role) " + "AND (:stringSearch = '' OR u.name ILIKE %:stringSearch% " + "OR u.email ILIKE %:stringSearch%)")
    Page<User> findAllFiltered(@Param("stringSearch") String stringSearch, @Param("role") UserRole role, Pageable pageable);
}
