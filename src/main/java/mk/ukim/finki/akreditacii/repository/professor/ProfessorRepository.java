package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProfessorRepository extends JpaRepository<Professor, String> {
    @Query("SELECT p FROM Professor p " + "WHERE (:title IS NULL OR p.title = :title) " + "AND (:stringSearch = '' OR p.name ILIKE %:stringSearch% " + "OR p.email ILIKE %:stringSearch%)")
    Page<Professor> findAllFiltered(@Param("stringSearch") String stringSearch, @Param("title") ProfessorTitle title, Pageable pageable);

    Page<Professor> findAll(Pageable pageable);




}
