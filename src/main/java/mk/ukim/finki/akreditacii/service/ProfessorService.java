package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfessorService {

    Professor getProfessorById(String professorId);

    List<Professor> listAll();

    Page<Professor> findAllWithPagination(int pageNum, int pageSize);

    Professor save(String id, String name, String email, ProfessorTitle title);

    void deleteById(String id);
}
