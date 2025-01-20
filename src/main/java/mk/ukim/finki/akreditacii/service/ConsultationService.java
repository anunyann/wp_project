package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.consultations.Consultation;
import mk.ukim.finki.akreditacii.model.consultations.ConsultationType;

import java.util.List;

public interface ConsultationService {
    List<Consultation> listNextWeekConsultationsByProfessor(String id, ConsultationType type);
}
