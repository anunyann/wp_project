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
public class Book {

    @Column(name="book_order")
    private Short order;

    private String author;

    private String title;

    private String publisher;

    public Short year;
}
