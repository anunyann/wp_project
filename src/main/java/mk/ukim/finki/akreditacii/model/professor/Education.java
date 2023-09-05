package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
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


    public Education(String professorId, EducationDegree degree, Short finishingYear, String institution, String discipline, String field, String area) {
        this.id = professorId + degree.toString() + finishingYear + (int) (Math.random() * 1000);
        this.degree = degree;
        this.finishingYear = finishingYear;
        this.institution = institution;
        this.discipline = discipline;
        this.field = field;
        this.area = area;
    }
}
