package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WeeklySubjectTopic {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SubjectDetails subjectDetails;

    private Short weekNumber;

    @Column(length = 4000)
    private String lectureTopic;

    @Column(length = 4000)
    private String exerciseTopic;


}
