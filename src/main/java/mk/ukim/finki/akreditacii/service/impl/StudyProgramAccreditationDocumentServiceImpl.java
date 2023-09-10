package mk.ukim.finki.akreditacii.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.akreditacii.model.accreditation.AccreditationDocumentTypes;
import mk.ukim.finki.akreditacii.model.accreditation.StudyProgramAccreditationDocument;
import mk.ukim.finki.akreditacii.repository.StudyProgramAccreditationDocumentRepository;
import mk.ukim.finki.akreditacii.repository.StudyProgramDetailsRepository;
import mk.ukim.finki.akreditacii.service.StudyProgramAccreditationDocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StudyProgramAccreditationDocumentServiceImpl implements StudyProgramAccreditationDocumentService {
    private final StudyProgramAccreditationDocumentRepository studyProgramAccreditationDocumentRepository;
    private final StudyProgramDetailsRepository studyProgramDetailsRepository;

    public StudyProgramAccreditationDocumentServiceImpl(StudyProgramAccreditationDocumentRepository studyProgramAccreditationDocumentRepository, StudyProgramDetailsRepository studyProgramDetailsRepository) {
        this.studyProgramAccreditationDocumentRepository = studyProgramAccreditationDocumentRepository;
        this.studyProgramDetailsRepository = studyProgramDetailsRepository;
    }

    @Override
    @Transactional
    public List<StudyProgramAccreditationDocument> findAllByStudyProgramDetails(String studyProgramDetailsId) {
        return studyProgramAccreditationDocumentRepository.findAllByStudyProgram(studyProgramDetailsRepository.findById(studyProgramDetailsId).get());
    }

    @Override
    public Optional<StudyProgramAccreditationDocument> findById(Long id) {
        return studyProgramAccreditationDocumentRepository.findById(id);
    }

    @Override
    public Optional<StudyProgramAccreditationDocument> save(String name, String fileExtension, AccreditationDocumentTypes type, String studyProgramDetailsId, MultipartFile file) {
        try {
            StudyProgramAccreditationDocument studyProgramAccreditationDocument = new StudyProgramAccreditationDocument();
            byte[] document = file.getBytes();
            if (!name.equals("")) {
                studyProgramAccreditationDocument.setName(name + "." + fileExtension);
                studyProgramAccreditationDocument.setType(type);
                studyProgramAccreditationDocument.setStudyProgram(studyProgramDetailsRepository.findById(studyProgramDetailsId).get());
                studyProgramAccreditationDocument.setDocument(document);
            } else {
                studyProgramAccreditationDocument.setName(type.name() + "." + fileExtension);
                studyProgramAccreditationDocument.setType(type);
                studyProgramAccreditationDocument.setStudyProgram(studyProgramDetailsRepository.findById(studyProgramDetailsId).get());
                studyProgramAccreditationDocument.setDocument(document);
            }
            return Optional.of(studyProgramAccreditationDocumentRepository.save(studyProgramAccreditationDocument));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        studyProgramAccreditationDocumentRepository.deleteById(id);
    }
}
