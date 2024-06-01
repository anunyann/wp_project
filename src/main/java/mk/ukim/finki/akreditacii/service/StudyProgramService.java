package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudyProgramService {

    List<StudyProgram> findAll();

    Page<StudyProgram> findAllWithPagination(Pageable pageable);

    Optional<StudyProgram> findById(String code);

    void deleteById(String code);

}
