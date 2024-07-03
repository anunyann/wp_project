package mk.ukim.finki.akreditacii.repository.tsv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import mk.ukim.finki.akreditacii.repository.ImportRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CsvImportRepository implements ImportRepository {

    private CsvMapper mapper = new CsvMapper();

    @Override
    public <T> List<T> readFile(MultipartFile file, Class<T> clazz) {
        List<T> enrollments = new ArrayList<>();
        CsvSchema schema = mapper.schemaFor(clazz)
                .withHeader()
                .withLineSeparator("\n")
                .withColumnSeparator('\t');

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            MappingIterator<T> r = mapper
                    .reader(clazz)
                    .with(schema)
                    .readValues(br);
            while (r.hasNext()) {
                enrollments.add(r.nextValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return enrollments;
    }

    @Override
    public <T> void writeToOutputStream(Class<T> clazz, List<T> invalidEnrollments, OutputStream outputStream) throws IOException{
        CsvSchema schema=mapper.schemaFor(clazz)
                .withHeader()
                .withLineSeparator("\n")
                .withColumnSeparator('\t');
        mapper.writer(schema).writeValue(outputStream,invalidEnrollments);
        outputStream.flush();

    }

}

