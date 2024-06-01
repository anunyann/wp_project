package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.semester.SemesterType;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "professor_accreditation_stats_view")
@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorAccreditationStats {

    @Id
    public String id;

    @ManyToOne
    public Professor professor;

    @Enumerated(EnumType.STRING)
    public StudyCycle cycle;

    @Enumerated(EnumType.STRING)
    public SemesterType semester;

    public String accreditationYear;

    public Integer numSubjects;

    public Double numSubjectParts;

}
