package mk.ukim.finki.akreditacii.repository.professor;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorAcademicTitles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorAcademicTitlesRepository extends JpaRepository<ProfessorAcademicTitles, String> {

    ProfessorAcademicTitles findByProfessor(Professor professor);
    void deleteByProfessor(Professor professor);
    ProfessorAcademicTitles findByAcademicTitleId(String id);
}
