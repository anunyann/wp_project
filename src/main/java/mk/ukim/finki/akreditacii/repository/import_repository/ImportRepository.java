package mk.ukim.finki.akreditacii.repository.import_repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ImportRepository {
    <T> List<T> readFile(MultipartFile file, Class<T> entityType);

    <T> void writeToOutputStream(Class<T> entityType, List<T> enrollments, OutputStream outputStream) throws IOException;

}
