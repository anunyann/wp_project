package mk.ukim.finki.akreditacii.model.subject;

import lombok.Data;

import java.util.List;

@Data
public class SubjectAllocationStatsDTO {
    Integer yearsActive;
    Double numberOfFirstTimeStudents;
    Double numberOfReEnrollmentStudents;

    public SubjectAllocationStatsDTO(Integer yearsActive, Double numberOfFirstTimeStudents, Double numberOfReEnrollmentStudents) {
        this.yearsActive = yearsActive;
        this.numberOfFirstTimeStudents = numberOfFirstTimeStudents;
        this.numberOfReEnrollmentStudents = numberOfReEnrollmentStudents;
    }

    public SubjectAllocationStatsDTO() {
    }
}
