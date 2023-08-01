package mk.ukim.finki.akreditacii.model.accreditation;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Accreditation {

    @Id
    private String year;

    private LocalDate activeFrom;

    private LocalDate activeTo;

    private Boolean isActive; // Only one accreditation is active in DB

    @ElementCollection
    private List<String> studyProgramFields;

    @Deprecated
    @Lob
    private byte[] accreditationDecisionDocument;

    public Accreditation(String year, LocalDate activeFrom, LocalDate activeTo, boolean isActive, List<String> studyProgramFields) {
        this.year = year;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
        this.isActive = isActive;
        this.studyProgramFields = studyProgramFields;
    }
}
