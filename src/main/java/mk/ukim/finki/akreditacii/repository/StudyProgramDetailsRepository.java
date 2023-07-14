package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyProgramDetailsRepository extends JpaRepository<StudyProgramDetails, String> {

    List<StudyProgramDetails> findAllByAccreditationYearAndStudyCycleOrderByOrderAsc(String accreditation, StudyCycle cycle);
}
