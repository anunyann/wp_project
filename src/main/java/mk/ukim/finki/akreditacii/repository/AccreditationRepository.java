package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccreditationRepository extends JpaRepository<Accreditation, String> {
    long countAccreditationsByIsActiveTrue();
    List<Accreditation> findAccreditationsByIsActiveTrue();

    Page<Accreditation> findAll(Pageable pageable);
}
