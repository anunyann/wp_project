package mk.ukim.finki.akreditacii.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "email", "role", "message"})

public class UserDto {

    private String id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String message;

    public UserDto(String id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
