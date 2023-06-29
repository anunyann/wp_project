package mk.ukim.finki.akreditacii.model.study_program;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.accreditation.AccreditationDescriptiveField;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyProgramDetails {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private StudyProgram studyProgram;

    private Float order;

    private Short durationYears;

    private Short durationSemesters;

    @Enumerated(EnumType.STRING)
    private StudyCycle studyCycle;

    @ManyToOne
    private Accreditation accreditation;

    @ElementCollection
    private List<AccreditationDescriptiveField> fields;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudyProgramDetails professor = (StudyProgramDetails) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
