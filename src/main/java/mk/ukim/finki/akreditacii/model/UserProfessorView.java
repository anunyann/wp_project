package mk.ukim.finki.akreditacii.model;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.room.Room;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_professor_view")
public class UserProfessorView {
    @Id
    private String id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private ProfessorTitle title;

    private Short orderingRank;

    @ManyToOne
    private Room office;
}
