package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.accreditation.AccreditationDocumentTypes;
import mk.ukim.finki.akreditacii.model.accreditation.StudyProgramAccreditationDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StudyProgramAccreditationDocumentService {
    List<StudyProgramAccreditationDocument> findAllByStudyProgramDetails(String studyProgramDetailsId);

    Optional<StudyProgramAccreditationDocument> findById(Long id);

    Optional<StudyProgramAccreditationDocument> save(String name, String fileExtension, AccreditationDocumentTypes type, String studyProgramDetailsId, MultipartFile document);

    void deleteById(Long id);
}
