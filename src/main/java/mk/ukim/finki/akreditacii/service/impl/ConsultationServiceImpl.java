package mk.ukim.finki.akreditacii.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.akreditacii.model.consultations.Consultation;
import mk.ukim.finki.akreditacii.model.consultations.ConsultationType;
import mk.ukim.finki.akreditacii.repository.ConsultationRepository;
import mk.ukim.finki.akreditacii.service.ConsultationService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationRepository consultationRepository;

    @Override
    public List<Consultation> listNextWeekConsultationsByProfessor(String id, ConsultationType type) {
        LocalDate today = LocalDate.now();

        LocalDate nextWeekStart = today.plusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate nextWeekEnd = nextWeekStart.plusDays(6);

        return consultationRepository.findConsultationsForNextWeek(
                id,
                type,
                nextWeekStart,
                nextWeekEnd
        );
    }
}
