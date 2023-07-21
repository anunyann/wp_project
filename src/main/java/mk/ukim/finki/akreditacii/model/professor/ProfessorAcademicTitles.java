package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfessorAcademicTitles {

    @Id
    private String id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private AcademicTitle academicTitle;
}
