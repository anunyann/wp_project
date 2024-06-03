package mk.ukim.finki.akreditacii.service;

import mk.ukim.finki.akreditacii.model.subject.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllSubjects();
    Subject findById(String id);
}
