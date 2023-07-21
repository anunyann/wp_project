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
public class Education {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private EducationDegree degree;

    private Short finishingYear;

    private String institution;

    private String discipline; // podracje

    private String field; // pole

    private String area; // oblast

}
