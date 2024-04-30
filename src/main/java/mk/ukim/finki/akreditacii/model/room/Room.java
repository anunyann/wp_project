package mk.ukim.finki.akreditacii.model.room;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Room {

    @Id
    private String name;

    private String locationDescription;

    private String equipmentDescription;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private Long capacity;

    public Room(String name, String locationDescription, String equipmentDescription, RoomType type, Long capacity) {
        this.name = name;
        this.locationDescription = locationDescription;
        this.equipmentDescription = equipmentDescription;
        this.type = type;
        this.capacity = capacity;
    }
}

