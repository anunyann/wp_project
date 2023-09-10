package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class ProfessorEducation {
    @Id
    private String id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private Education education;

    private Float displayOrder;

    public ProfessorEducation(String educationId, Professor professor, Education education, Float displayOrder) {
        this.id = educationId + (int) (Math.random() * 1000);
        this.professor = professor;
        this.education = education;
        this.displayOrder = displayOrder;
    }
}
