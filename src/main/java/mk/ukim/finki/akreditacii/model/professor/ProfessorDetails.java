package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfessorDetails {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Professor professor;

    private Float order;

    @Enumerated(EnumType.STRING)
    private EducationDegree degree;

    private String degreeTitle;

    private LocalDate birthDay;

    @ManyToOne
    private ProfessorAcademicTitles currentTitle;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProfessorDetails professor = (ProfessorDetails) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
