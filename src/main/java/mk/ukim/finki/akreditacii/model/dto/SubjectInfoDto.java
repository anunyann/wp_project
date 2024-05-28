package mk.ukim.finki.akreditacii.model.dto;

import lombok.Data;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;

import java.util.List;

@Data
public class SubjectInfoDto {

    public SubjectDetails subject;

    public List<Professor> professors;

    public List<StudyProgram> mandatoryStudyPrograms;

    public List<StudyProgram> electiveStudyPrograms;
}
