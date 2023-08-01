package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccreditationService {
    List<Accreditation> findAll();

    Optional<Accreditation> findById(String year);

    void deleteById(String year);

    Optional<Accreditation> save(String year, LocalDate activeFrom, LocalDate activeTo, List<String> studyProgramFields);

    void activate(String year);
}
