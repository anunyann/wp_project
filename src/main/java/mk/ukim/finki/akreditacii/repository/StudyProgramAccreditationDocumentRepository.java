package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.accreditation.StudyProgramAccreditationDocument;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgramDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyProgramAccreditationDocumentRepository extends JpaRepository<StudyProgramAccreditationDocument, Long> {
    List<StudyProgramAccreditationDocument> findAllByStudyProgram(StudyProgramDetails studyProgramDetails);
}
