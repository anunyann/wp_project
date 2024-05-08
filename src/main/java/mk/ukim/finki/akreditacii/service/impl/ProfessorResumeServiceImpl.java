package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidProfessorId;
import mk.ukim.finki.akreditacii.model.professor.ProfessorResume;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorResumeRepository;
import mk.ukim.finki.akreditacii.service.ProfessorResumeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorResumeServiceImpl implements ProfessorResumeService {

    private final ProfessorResumeRepository professorResumeRepository;

    public ProfessorResumeServiceImpl(ProfessorResumeRepository professorResumeRepository) {
        this.professorResumeRepository = professorResumeRepository;
    }

    @Override
    public Optional<ProfessorResume> findById(String id) {
        return this.professorResumeRepository.findById(id);
    }

    @Override
    public ProfessorResume save(String professorId, String resume, byte[] image) {
        ProfessorResume professorResume = this.professorResumeRepository.findById(professorId).orElseThrow(() -> new InvalidProfessorId(professorId));
        professorResume.setResume(resume);
        professorResume.setImage(image);

        return this.professorResumeRepository.save(professorResume);
    }

    @Override
    public ProfessorResume update(ProfessorResume obj) {
        ProfessorResume professorResume = this.professorResumeRepository.findById(obj.getId()).orElseThrow(() -> new InvalidProfessorId(obj.getId()));

        professorResume.setResume(obj.getResume());
        professorResume.setImage(obj.getImage());

        return this.professorResumeRepository.save(professorResume);
    }
}
