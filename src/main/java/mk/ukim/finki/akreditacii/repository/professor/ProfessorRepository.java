package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.professor.dto.ProfessorNameAndCodeDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProfessorRepository extends JpaRepository<Professor, String> {
    @Query("SELECT p FROM Professor p " + "WHERE (:title IS NULL OR p.title = :title) " + "AND (:stringSearch = '' OR p.name ILIKE %:stringSearch% " + "OR p.email ILIKE %:stringSearch%) ORDER BY p.name ASC ")
    Page<Professor> findAllFiltered(@Param("stringSearch") String stringSearch, @Param("title") ProfessorTitle title, Pageable pageable);

    Page<Professor> findAllByOrderByNameAsc(Pageable pageable);

    Page<Professor> findAll(Pageable pageable);
    @Query("SELECT new mk.ukim.finki.akreditacii.model.professor.dto.ProfessorNameAndCodeDTO(" +
            "p.id, " +
            "p.name) " +
            "FROM Professor p ")
    List<ProfessorNameAndCodeDTO> findAllNameAndCode();

    Optional<Professor> findByIdIsNotAndEmail(String id,String email);

}
