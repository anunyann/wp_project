package mk.ukim.finki.akreditacii.model.subject;

import jakarta.persistence.Column;
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
public class SubjectDependenciess {

    @Column(length = 5000)
    private String dependencies;
}
