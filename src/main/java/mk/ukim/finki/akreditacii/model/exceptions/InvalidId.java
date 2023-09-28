package mk.ukim.finki.akreditacii.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidId extends RuntimeException{
    public InvalidId(String id){
        super("Invalid id: "+ id);
    }
}
