package mk.ukim.finki.akreditacii.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import mk.ukim.finki.akreditacii.model.room.RoomType;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"Name", "Location", "Equipment", "Type", "Capacity"})
public class RoomDto {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("Equipment")
    private String equipment;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Capacity")
    private String capacity;


}
