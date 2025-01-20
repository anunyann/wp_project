package mk.ukim.finki.akreditacii.model.consultations;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.room.Room;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Consultation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private Room room;

    @Enumerated(EnumType.STRING)
    private ConsultationType type;

    private LocalDate oneTimeDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek weeklyDayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @ElementCollection
    private List<LocalDate> canceledDates;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;

    private Boolean online;

    private String studentInstructions;

    public String getMacedonianDayOfWeek() {
        Map<DayOfWeek, String> daysInMacedonian = Map.of(
                DayOfWeek.MONDAY, "понеделник",
                DayOfWeek.TUESDAY, "вторник",
                DayOfWeek.WEDNESDAY, "среда",
                DayOfWeek.THURSDAY, "четврток",
                DayOfWeek.FRIDAY, "петок",
                DayOfWeek.SATURDAY, "сабота",
                DayOfWeek.SUNDAY, "недела"
        );
        return daysInMacedonian.getOrDefault(this.weeklyDayOfWeek, "Непознат ден");
    }
}
