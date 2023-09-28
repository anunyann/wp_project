package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyProgramDetailsRepository extends JpaRepository<StudyProgramDetails, String> {

    Page<StudyProgramDetails> findAll(Pageable pageable);

    List<StudyProgramDetails> findAllByAccreditationYearAndStudyCycleOrderByOrderAsc(String accreditation, StudyCycle cycle);

    @Query("SELECT spd FROM StudyProgramDetails spd " +
            "WHERE (:nameSearch = '' OR spd.studyProgram.name ILIKE %:nameSearch%)" +
            "AND (:accreditationYear = '' OR spd.accreditation.year = :accreditationYear) " +
            "AND (:studyCycle = null OR spd.studyCycle = :studyCycle) " +
            "AND (:durationYears = null OR spd.durationYears = :durationYears) " +
            "AND (:onEnglish = false OR spd.onEnglish = :onEnglish)")
    Page<StudyProgramDetails> findAllFiltered(
            @Param("nameSearch") String nameSearch,
            @Param("accreditationYear") String accreditationYear,
            @Param("studyCycle") StudyCycle studyCycle,
            @Param("durationYears") Short durationYears,
            @Param("onEnglish") Boolean onEnglish,
            Pageable pageable
    );
}
