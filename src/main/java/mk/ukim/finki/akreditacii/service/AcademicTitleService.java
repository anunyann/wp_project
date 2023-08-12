package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;

import java.util.List;
import java.util.Optional;

public interface AcademicTitleService  {
    Optional<AcademicTitle> save(String id, String institution, ProfessorTitle title, String area, Short electionYear, Short decisionDocumentNumber);
    List<AcademicTitle> listAll();
}
