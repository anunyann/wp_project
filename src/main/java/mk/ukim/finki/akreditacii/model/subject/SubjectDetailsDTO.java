package mk.ukim.finki.akreditacii.model.subject;
import java.util.Date;
import java.util.List;

public class SubjectDetailsDTO {
    private String code;
    private String name;
    private List<StudyProgramSubject> studyPrograms;
    private int numberOfContents;
    private Date lastModified;
    private boolean locked;

    public SubjectDetailsDTO() {
    }

    public SubjectDetailsDTO(String code, String name, List<StudyProgramSubject> studyPrograms, int numberOfContents, Date lastModified,boolean locked) {
        this.code = code;
        this.name = name;
        this.studyPrograms = studyPrograms;
        this.numberOfContents = numberOfContents;
        this.lastModified = lastModified;
        this.locked = locked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudyProgramSubject> getStudyPrograms() {
        return studyPrograms;
    }

    public void setStudyPrograms(List<StudyProgramSubject> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    public int getNumberOfContents() {
        return numberOfContents;
    }

    public void setNumberOfContents(int numberOfContents) {
        this.numberOfContents = numberOfContents;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "YourEntityDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", studyPrograms=" + studyPrograms +
                ", numberOfContents=" + numberOfContents +
                ", lastModified=" + lastModified +
                ", locked=" + locked +
                '}';
    }
}
