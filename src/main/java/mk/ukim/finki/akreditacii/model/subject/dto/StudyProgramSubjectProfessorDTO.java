package mk.ukim.finki.akreditacii.model.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyProgramSubjectProfessorDTO {
    private String subjectId;
    private String professorId;
    private String studyProgramId;
    private Boolean studyProgramMandatory;
}