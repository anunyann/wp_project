package mk.ukim.finki.akreditacii.web;

import mk.ukim.finki.akreditacii.model.accreditation.AccreditationDocumentTypes;
import mk.ukim.finki.akreditacii.model.accreditation.StudyProgramAccreditationDocument;
import mk.ukim.finki.akreditacii.service.StudyProgramAccreditationDocumentService;
import mk.ukim.finki.akreditacii.service.StudyProgramDetailsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
@RequestMapping("admin/study-programs/{studyProgramId}/documents")
public class StudyProgramAccreditationDocumentManagement {
    private final StudyProgramAccreditationDocumentService studyProgramAccreditationDocumentService;
    private final StudyProgramDetailsService studyProgramDetailsService;

    public StudyProgramAccreditationDocumentManagement(StudyProgramAccreditationDocumentService studyProgramAccreditationDocumentService, StudyProgramDetailsService studyProgramDetailsService) {
        this.studyProgramAccreditationDocumentService = studyProgramAccreditationDocumentService;
        this.studyProgramDetailsService = studyProgramDetailsService;
    }

    @GetMapping
    public String allDocuments(Model model, @PathVariable String studyProgramId) {

        List<StudyProgramAccreditationDocument> studyProgramAccreditationDocuments = studyProgramAccreditationDocumentService.findAllByStudyProgramDetails(studyProgramId);

        model.addAttribute("studyProgramAccreditationDocuments", studyProgramAccreditationDocuments);
        model.addAttribute("studyProgramId", studyProgramId);
        model.addAttribute("accreditationDocumentTypes", AccreditationDocumentTypes.values());

        return "study_program/study_program_accreditation_document_list";
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    @PostMapping("/add")
    public String addDocument(@RequestParam(required = false) String name, @RequestParam AccreditationDocumentTypes type, @PathVariable String studyProgramId, @RequestParam MultipartFile file) {
        studyProgramAccreditationDocumentService.save(name, getFileExtension(file.getOriginalFilename()), type, studyProgramId, file).get();

        return "redirect:/admin/study-programs/" + studyProgramId + "/documents";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable Long id) throws FileNotFoundException {
        StudyProgramAccreditationDocument document = studyProgramAccreditationDocumentService.findById(id).orElseThrow(() -> new FileNotFoundException("Document not found"));
        ByteArrayResource resource = new ByteArrayResource(document.getDocument());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")

                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(document.getDocument().length)
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(@PathVariable String studyProgramId, @PathVariable Long id) {
        studyProgramAccreditationDocumentService.deleteById(id);

        return "redirect:/admin/study-programs/" + studyProgramId + "/documents";
    }

}
