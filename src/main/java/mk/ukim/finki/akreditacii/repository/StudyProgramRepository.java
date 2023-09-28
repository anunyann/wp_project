package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, String> {
    Page<StudyProgram> findAll(Pageable pageable);
}
