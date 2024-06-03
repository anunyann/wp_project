package mk.ukim.finki.akreditacii.model.subject.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubjectStatisticsDTO {
    String subjectCode;
    List<String> professorCodes;
    Integer professorNumber;
    List<String> mandatoryStudyPrograms;
    Integer yearsActive;
    Double numberOfFirstTimeStudents;
    Double numberOfReEnrollmentStudents;

    public SubjectStatisticsDTO(String subjectCode, List<String> professorCodes, Integer professorNumber, List<String> mandatoryStudyPrograms, Integer yearsActive, Double numberOfFirstTimeStudents, Double numberOfReEnrollmentStudents) {
        this.subjectCode = subjectCode;
        this.professorCodes = professorCodes;
        this.professorNumber = professorNumber;
        this.mandatoryStudyPrograms = mandatoryStudyPrograms;
        this.yearsActive = yearsActive;
        this.numberOfFirstTimeStudents = numberOfFirstTimeStudents;
        this.numberOfReEnrollmentStudents = numberOfReEnrollmentStudents;
    }

    public SubjectStatisticsDTO() {
    }
}
