package mk.ukim.finki.akreditacii.model.dto;

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
}
