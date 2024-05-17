package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.akreditacii.model.semester.SemesterType;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {

    @Id
    private String id; // code

    @Column(nullable = false)
    private String name;

    private String abbreviation;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    private Integer weeklyLecturesClasses;

    private Integer weeklyAuditoriumClasses;

    private Integer weeklyLabClasses;

    // todo: credits, nameEn

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subject subject = (Subject) o;
        return getId() != null && Objects.equals(getId(), subject.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
