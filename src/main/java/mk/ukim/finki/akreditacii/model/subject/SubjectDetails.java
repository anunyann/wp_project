package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.semester.SemesterType;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject")
public class SubjectDetails {

    @Id
    @Column(name = "id")
    private String id;

    @Column(nullable = false)
    private String name;

    private String abbreviation;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    private Integer weeklyLecturesClasses;

    private Integer weeklyAuditoriumClasses;

    private Integer weeklyLabClasses;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Subject subject;

    private Boolean placeholder;

    private String nameEn;

    private Short defaultSemester;

    private Float credits;

    @Enumerated(EnumType.STRING)
    private StudyCycle cycle;


    private String language;


    @Column(length = 8000)
    private String learningMethods;


    @Column(length = 8000)
    private String goalsDescription;

    @Column(length = 8000)
    private String content;

    @Column(length = 8000)
    private String goalsDescriptionEn;

    @Column(length = 8000)
    private String contentEn;

    @Column(length = 4000)
    private String qualityControl;


    @ManyToOne(fetch = FetchType.LAZY)
    private Accreditation accreditation;

    @Embedded
    private SubjectObligationDuration obligationDuration;

    @Embedded
    private SubjectDependencies dependencies;

    @Embedded
    private SubjectGrading grading;

    @Embedded
    private SubjectBibliography bibliography;

    @ElementCollection
    private List<String> notes;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubjectDetails professor = (SubjectDetails) o;
        return getId() != null && Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
