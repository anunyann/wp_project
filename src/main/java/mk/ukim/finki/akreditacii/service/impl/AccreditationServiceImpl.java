package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidAccreditation;
import mk.ukim.finki.akreditacii.model.exceptions.NoActiveAccreditation;
import mk.ukim.finki.akreditacii.repository.AccreditationRepository;
import mk.ukim.finki.akreditacii.service.AccreditationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccreditationServiceImpl implements AccreditationService {
    private final AccreditationRepository accreditationRepository;

    public AccreditationServiceImpl(AccreditationRepository accreditationRepository) {
        this.accreditationRepository = accreditationRepository;
    }

    @Override
    public List<Accreditation> findAll() {
        return accreditationRepository.findAll();
    }

    @Override
    public Page<Accreditation> findAllWithPagination(Pageable pageable) {
        return accreditationRepository.findAll(pageable);
    }

    @Override
    public Optional<Accreditation> findById(String year) {
        return accreditationRepository.findById(year);
    }

    @Override
    public void deleteById(String year) {
        accreditationRepository.deleteById(year);
    }

    @Override
    public Optional<Accreditation> save(String year, LocalDate activeFrom, LocalDate activeTo, List<String> studyProgramFields) {
        Accreditation accreditation;
        if (accreditationRepository.findById(year).isPresent()) {
            accreditation = accreditationRepository.findById(year).get();
            accreditation.setActiveFrom(activeFrom);
            accreditation.setActiveTo(activeTo);
        } else {
            accreditation = new Accreditation(year, activeFrom, activeTo, false, studyProgramFields);
        }
        return Optional.of(accreditationRepository.save(accreditation));
    }

    @Override
    public void activate(String year) {
        if (accreditationRepository.countAccreditationsByIsActiveTrue() > 0) {
            List<Accreditation> activeAccreditations = accreditationRepository.findAccreditationsByIsActiveTrue();
            for (Accreditation acc : activeAccreditations) {
                acc.setIsActive(false);
            }
            accreditationRepository.saveAll(activeAccreditations);
        } else {
            throw new NoActiveAccreditation(year);
        }

        if (accreditationRepository.findById(year).isPresent()) {
            Accreditation inactiveAccreditation = accreditationRepository.findById(year).get();
            inactiveAccreditation.setIsActive(true);
            accreditationRepository.save(inactiveAccreditation);
        } else throw new InvalidAccreditation(year);

    }

}
