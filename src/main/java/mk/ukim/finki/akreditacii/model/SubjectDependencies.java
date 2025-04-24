package mk.ukim.finki.akreditacii.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subject_dependencies")
public class SubjectDependencies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_code", nullable = false)
    private String subjectCode;

    @Column(name = "study_program_code")
    private String studyProgramCode; // Optional - for program-specific dependencies

    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type", nullable = false)
    private SubjectDependencyType dependencyType;

    @Column(name = "dependency", nullable = false)
    private String dependency;

    // Constructors
    public SubjectDependencies() {
    }

    public SubjectDependencies(String subjectCode, String studyProgramCode,
                               SubjectDependencyType dependencyType, String dependency) {
        this.subjectCode = subjectCode;
        this.studyProgramCode = studyProgramCode;
        this.dependencyType = dependencyType;
        this.dependency = dependency;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getStudyProgramCode() {
        return studyProgramCode;
    }

    public void setStudyProgramCode(String studyProgramCode) {
        this.studyProgramCode = studyProgramCode;
    }

    public SubjectDependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(SubjectDependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }
}