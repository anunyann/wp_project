package mk.ukim.finki.akreditacii.model.professor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mk.ukim.finki.akreditacii.model.semester.Semester;
import mk.ukim.finki.akreditacii.model.subject.JoinedSubject;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class TeacherSubjectAllocations {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private JoinedSubject subject;

    private Boolean englishGroup;

    @ManyToOne
    private Semester semester;

    @Deprecated
    private Float totalLectureClasses;
    @Deprecated
    private Float totalExerciseClasses;
    @Deprecated
    private Float totalLabClasses;

    private Float numberOfLectureGroups;
    private Float numberOfExerciseGroups;
    private Float numberOfLabGroups;


    @Deprecated
    private Integer totalLectureStudents;
    @Deprecated
    private Integer totalExerciseStudents;
    @Deprecated
    private Integer totalLabStudents;

    @Column(length = 4_000)
    private String validationMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeacherSubjectAllocations that = (TeacherSubjectAllocations) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
