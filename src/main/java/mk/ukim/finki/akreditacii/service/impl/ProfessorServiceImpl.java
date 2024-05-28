package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.professor.dto.ProfessorNameAndCodeDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Page<Professor> findAllWithPagination(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return professorRepository.findAll(pageRequest);
    }

    @Override
    public Page<Professor> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredTitle) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        return professorRepository.findAllFiltered(stringSearch,
                !filteredTitle.equals("") ? ProfessorTitle.valueOf(filteredTitle) : null,
                pageRequest);
    }


    @Override
    public Professor getProfessorById(String professorId) {
        return professorRepository.findById(professorId).orElseThrow(() -> new InvalidId(professorId));
    }

    @Override
    public List<ProfessorNameAndCodeDTO> findAllProfessorNameAndCode() {
        Collator macedonianCollator = Collator.getInstance(new Locale("mk"));

        return this.professorRepository.findAllNameAndCode().stream()
                .sorted(Comparator.comparing(ProfessorNameAndCodeDTO::getName, (c1, c2) -> {
                    return macedonianCollator.compare(String.valueOf(c1), String.valueOf(c2));
                }))
                .collect(Collectors.toList());
    }

    @Override
    public Professor save(String id, String name, String email, ProfessorTitle title) {
        Professor professor = new Professor(id, name, email, title);
        return professorRepository.save(professor);
    }

    @Override
    public void deleteById(String id) {
        professorRepository.deleteById(id);
    }

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }
}
