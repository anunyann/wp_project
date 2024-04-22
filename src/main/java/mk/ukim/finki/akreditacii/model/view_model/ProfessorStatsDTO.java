package mk.ukim.finki.akreditacii.model.view_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.akreditacii.model.professor.Professor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorStatsDTO {
    Professor professor;

    Integer firstCycleTotal;

    Integer secondCycleTotal;

    Integer thirdCycleTotal;

    Double sumPerCycleFirst;

    Double sumPerCycleSecond;

    Double sumPerCycleThird;

    public ProfessorStatsDTO(Professor professor, Integer firstCycleTotal, Integer secondCycleTotal, Integer thirdCycleTotal) {
        this.professor = professor;
        this.firstCycleTotal = firstCycleTotal;
        this.secondCycleTotal = secondCycleTotal;
        this.thirdCycleTotal = thirdCycleTotal;
    }
}
