package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, String> {

    Page<SubjectDetails> findAll(Pageable pageable);

    @Query("SELECT sd FROM SubjectDetails sd " +
            "WHERE (:nameSearch is null OR sd.subject.name ILIKE %:nameSearch%) " +
            "AND (:accreditationYear is null OR sd.accreditation.year = :accreditationYear)")
    Page<SubjectDetails> findAllFiltered(
            @Param("nameSearch") String nameSearch,
            @Param("accreditationYear") String accreditationYear,
            Pageable pageable
    );


}
