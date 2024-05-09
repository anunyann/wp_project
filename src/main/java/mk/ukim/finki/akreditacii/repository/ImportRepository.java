package mk.ukim.finki.akreditacii.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ImportRepository {


    <T> List<T> readRooms(MultipartFile file, Class<T> clazz);

    <T> void writeRooms(Class<T> clazz, List<T> invalidRooms, OutputStream outputStream) throws IOException;

}
