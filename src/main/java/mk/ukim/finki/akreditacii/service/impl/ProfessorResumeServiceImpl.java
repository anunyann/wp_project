package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.exceptions.InvalidProfessorId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorResume;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorResumeRepository;
import mk.ukim.finki.akreditacii.service.ProfessorResumeService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfessorResumeServiceImpl implements ProfessorResumeService {

    private final ProfessorResumeRepository professorResumeRepository;
    private final ProfessorRepository professorRepository;

    public ProfessorResumeServiceImpl(ProfessorResumeRepository professorResumeRepository, ProfessorRepository professorRepository) {
        this.professorResumeRepository = professorResumeRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public Optional<ProfessorResume> findById(String id) {
        return this.professorResumeRepository.findById(id);
    }

    @Override
    public ProfessorResume save(String professorId, String resume, MultipartFile image) {
        Professor professor = this.professorRepository.findById(professorId)
                .orElseThrow(() -> new InvalidProfessorId(professorId));

        Optional<ProfessorResume> professorResumeOpt = this.professorResumeRepository.findById(professorId);

        ProfessorResume professorResume = professorResumeOpt.orElseGet(() -> new ProfessorResume(professorId, professor, null, null));

        professorResume.setProfessor(professor);
        professorResume.setResume(resume);

        try {
            if (image != null && !image.isEmpty()) {
                if (!image.getContentType().startsWith("image")) {
                    throw new IllegalArgumentException("Uploaded file is not an image");
                }
                professorResume.setImage(image.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image bytes", e);
        }

        return this.professorResumeRepository.save(professorResume);
    }

    @Override
    public ProfessorResume update(ProfessorResume obj) {
        ProfessorResume professorResume = this.professorResumeRepository.findById(obj.getId()).orElseThrow(() -> new InvalidProfessorId(obj.getId()));

        professorResume.setResume(obj.getResume());
        professorResume.setImage(obj.getImage());

        return this.professorResumeRepository.save(professorResume);
    }

    @Override
    public ProfessorResume getByProfessorId(String id) {
        return professorResumeRepository.findByProfessorId(id);
    }
}
