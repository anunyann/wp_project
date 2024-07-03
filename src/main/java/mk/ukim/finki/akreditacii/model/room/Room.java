package mk.ukim.finki.akreditacii.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"Name", "Location", "Equipment", "Type", "Capacity"})
public class Room {

    @Id
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Location")
    private String locationDescription;
    @JsonProperty("Equipment")
    private String equipmentDescription;
    @JsonProperty("Type")
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @JsonProperty("Capacity")
    private Long capacity;

    public Room(String name, String locationDescription, String equipmentDescription, RoomType type, Long capacity) {
        this.name = name;
        this.locationDescription = locationDescription;
        this.equipmentDescription = equipmentDescription;
        this.type = type;
        this.capacity = capacity;
    }
}

