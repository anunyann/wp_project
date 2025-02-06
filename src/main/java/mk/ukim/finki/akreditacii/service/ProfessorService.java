package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.professor.dto.ProfessorNameAndCodeDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProfessorService {

    Professor getProfessorById(String professorId);

    List<ProfessorNameAndCodeDTO> findAllProfessorNameAndCode();

    Page<Professor> findAllWithPagination(int pageNum, int pageSize);

    Page<Professor> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredTitle);

    Professor save(String id, String name, String email, ProfessorTitle title);

    void deleteById(String id);

    List<Professor> findAll();

}
