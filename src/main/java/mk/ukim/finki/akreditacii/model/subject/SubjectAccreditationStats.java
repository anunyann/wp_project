package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "stats_per_subject_view")
@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectAccreditationStats {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String subjectName;

    @Column(name = "accreditation_year")
    private String accreditationYear;

    @Column(name = "mandatory_study_programs")
    private String mandatoryStudyPrograms;

    @Column(name = "all_study_programs")
    private String allStudyPrograms;

    @Column(name = "num_professors")
    private Long numProfessors;

    @Column(name = "professors")
    private String professors;

    @Column(name = "years_active")
    private Long yearsActive;

    @Column(name = "first_time_students")
    private Integer firstTimeStudents;

    @Column(name = "re_enrolled_students")
    private Integer reEnrolledStudents;
}
