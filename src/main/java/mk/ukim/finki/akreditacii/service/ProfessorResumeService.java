package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.ProfessorResume;

import java.util.Optional;

public interface ProfessorResumeService {
    Optional<ProfessorResume> findById(String id);
    ProfessorResume save(String professorId, String resume, byte[] image);
    ProfessorResume update(ProfessorResume obj);
}
