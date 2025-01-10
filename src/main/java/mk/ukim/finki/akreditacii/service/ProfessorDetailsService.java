package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;

import java.util.List;

public interface ProfessorDetailsService {

    ProfessorDetails save(ProfessorDetails professorDetails);

    ProfessorDetails findById(String id);

    List<ProfessorDetails> findAll();

    void deleteById(String id);

    ProfessorDetails getByProfessorId(String id);
}
