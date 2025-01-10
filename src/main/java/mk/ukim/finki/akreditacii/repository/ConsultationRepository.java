package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.consultations.Consultation;
import mk.ukim.finki.akreditacii.model.consultations.ConsultationType;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationRepository extends JpaSpecificationRepository<Consultation, Long> {
    @Query("SELECT c FROM Consultation c WHERE c.professor.id = :professorId AND c.type = :type " +
            "AND c.status = mk.ukim.finki.akreditacii.model.consultations.ConsultationStatus.ACTIVE " +
            "AND c.oneTimeDate BETWEEN :startDate AND :endDate " +
            "ORDER BY c.oneTimeDate, c.startTime")
    List<Consultation> findConsultationsForNextWeek(String professorId, ConsultationType type, LocalDate startDate, LocalDate endDate);
}
