package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;

public interface ProfessorService {

    Professor getProfessorById(String professorId);

    Page<Professor> findAllWithPagination(int pageNum, int pageSize);

    Professor save(String id, String name, String email, ProfessorTitle title);

    void deleteById(String id);
}
