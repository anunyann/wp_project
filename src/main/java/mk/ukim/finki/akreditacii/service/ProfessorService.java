package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.dto.ProfessorStatsDTO;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfessorService {

    Professor getProfessorById(String professorId);

    Page<Professor> findAllWithPagination(int pageNum, int pageSize);

    Page<Professor> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredTitle);

    Professor save(String id, String name, String email, ProfessorTitle title);

    void deleteById(String id);

    List<Professor> findAll();

    Page<ProfessorStatsDTO> getStatsForAllProfessorsWithPaginationAndFilters(Integer pageNum, Integer results, String stringSearch, String filteredTitle);
}
