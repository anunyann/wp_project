package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudyProgramSubject {

    @Id
    private String id;

    @ManyToOne
    private SubjectDetails subject;

    @ManyToOne
    private StudyProgram studyProgram;

    private Boolean mandatory;

    private Short semester;

    private Float order;

    private String subjectGroup;

    @Column(length = 5000)
    private String dependenciesOverride;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudyProgramSubject professor = (StudyProgramSubject) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
