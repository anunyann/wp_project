package mk.ukim.finki.akreditacii.model.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectAllocationStatsDTO {
    private String id;
    private Long yearsActive;
    private Double numberOfFirstTimeStudents;
    private Double numberOfReEnrollmentStudents;
}
