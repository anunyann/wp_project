package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AcademicTitle {

    @Id
    private String id;

    private String institution;

    @Enumerated(EnumType.STRING)
    private ProfessorTitle title;

    private String area;

    private Short electionYear;

    private Short decisionDocumentNumber;


}
