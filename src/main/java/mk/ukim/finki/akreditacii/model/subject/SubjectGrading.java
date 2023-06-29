package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectGrading {

    private Short testsPoints;

    private Short projectPoints;

    private Short activityPoints;

    private Short examPoints;

    private String signatureCondition;
}
