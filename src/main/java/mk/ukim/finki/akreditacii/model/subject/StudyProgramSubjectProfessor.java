package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyProgramSubjectProfessor {

    @Id
    private String id;

    @ManyToOne
    private StudyProgramSubject studyProgramSubject;

    @ManyToOne
    private Professor professor;

    private Float order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudyProgramSubjectProfessor professor = (StudyProgramSubjectProfessor) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
