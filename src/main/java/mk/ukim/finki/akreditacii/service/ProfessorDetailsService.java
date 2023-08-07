package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.ProfessorDetails;

import java.util.List;

public interface ProfessorDetailsService {

    ProfessorDetails findById(String id);
    ProfessorDetails findProfessorById(String id);
    List<ProfessorDetails> findAll();
}
