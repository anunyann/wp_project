package mk.ukim.finki.akreditacii.repository.import_repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ImportRepository {
    <T> List<T> readEnrolments(MultipartFile file, Class<T> entityType);

    <T> List<T> readPreferences(MultipartFile file, Class<T> clazz);

    <T> void writeEnrollments(Class<T> entityType, List<T> enrollments, OutputStream outputStream) throws IOException;

    <T> void writePreferences(Class<T> clazz, List<T> invalidPreferences, OutputStream outputStream) throws IOException;
}
