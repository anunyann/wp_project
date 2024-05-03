package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.dto.ProfessorStatsDTO;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository, StudyProgramSubjectProfessorRepository studyProgramSubjectProfessorRepository) {
        this.professorRepository = professorRepository;
        this.studyProgramSubjectProfessorRepository = studyProgramSubjectProfessorRepository;
    }

    @Override
    public Page<Professor> findAllWithPagination(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        return professorRepository.findAll(pageRequest);
    }

    @Override
    public Page<Professor> findAllWithPaginationFiltered(Integer pageNum, Integer results, String stringSearch, String filteredTitle) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);

        return professorRepository.findAllFiltered(stringSearch,
                !filteredTitle.equals("") ? ProfessorTitle.valueOf(filteredTitle) : null,
                pageRequest);
    }


    @Override
    public Professor getProfessorById(String professorId) {
        return professorRepository.findById(professorId).orElseThrow(() -> new InvalidId(professorId));
    }

    @Override
    public Professor save(String id, String name, String email, ProfessorTitle title) {
        Professor professor = new Professor(id, name, email, title);
        return professorRepository.save(professor);
    }

    @Override
    public void deleteById(String id) {
        professorRepository.deleteById(id);
    }

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    @Override
    public Page<ProfessorStatsDTO> getStatsForAllProfessorsWithPaginationAndFilters(Integer pageNum, Integer results, String stringSearch, String filteredTitle) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);
        List<Professor> professorList;
        List<ProfessorStatsDTO> professorStatsList = new ArrayList<>();

        if (stringSearch != null && filteredTitle != null) {
            professorList = professorRepository.findAllFilteredWithoutPaging(stringSearch,
                    !filteredTitle.isEmpty() ? ProfessorTitle.valueOf(filteredTitle) : null);
        } else {
            professorList = professorRepository.findAll();
        }

        int mainListSize = professorList.size();
        int startIndex = (pageNum - 1) * results;
        int endIndex = Math.min(startIndex + results, professorList.size());

        if (endIndex < startIndex || startIndex < 0) {
            startIndex = 0;
            endIndex = Math.min(results, mainListSize);
        } else if (endIndex > mainListSize) {
            endIndex = mainListSize;
        }
        professorList = professorList.subList(startIndex, endIndex);

        for (Professor professor : professorList) {
            double sumOfSubjectPartsPerCycleFirst = 0, sumOfSubjectPartsPerCycleSecond = 0, sumOfSubjectPartsPerCycleThird = 0;
            int countFirstCycle = 0, countSecondCycle = 0, countThirdCycle = 0;
            List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList = studyProgramSubjectProfessorRepository.findAllByProfessorId(professor.getId());
            List<StudyProgramSubjectProfessor> professorSubjects = studyProgramSubjectProfessorList.stream().filter(studyProgramSubjectProfessor -> studyProgramSubjectProfessor.getProfessor().equals(professor)).toList();

            for (var professorSubject : professorSubjects) {
                var subjectDetails = professorSubject.getStudyProgramSubject().getSubject();
                if (subjectDetails.getCycle().equals(StudyCycle.UNDERGRADUATE)) {
                    countFirstCycle += 1;
                    var subject = subjectDetails.getSubject();
                    List<StudyProgramSubjectProfessor> listOfSubjectProfessors = studyProgramSubjectProfessorList.stream().filter(studyProgramSubjectProfessor -> studyProgramSubjectProfessor.getStudyProgramSubject().getSubject().getSubject().equals(subject)).toList();
                    sumOfSubjectPartsPerCycleFirst += 1.0 / listOfSubjectProfessors.size();
                }
                if (subjectDetails.getCycle().equals(StudyCycle.MASTER)) {
                    countSecondCycle += 1;
                    var subject = subjectDetails.getSubject();
                    List<StudyProgramSubjectProfessor> listOfSubjectProfessors = studyProgramSubjectProfessorList.stream().filter(studyProgramSubjectProfessor -> studyProgramSubjectProfessor.getStudyProgramSubject().getSubject().getSubject().equals(subject)).toList();
                    sumOfSubjectPartsPerCycleSecond += 1.0 / listOfSubjectProfessors.size();
                }
                if (subjectDetails.getCycle().equals(StudyCycle.PHD)) {
                    countThirdCycle += 1;
                    var subject = subjectDetails.getSubject();
                    List<StudyProgramSubjectProfessor> listOfSubjectProfessors = studyProgramSubjectProfessorList.stream().filter(studyProgramSubjectProfessor -> studyProgramSubjectProfessor.getStudyProgramSubject().getSubject().getSubject().equals(subject)).toList();
                    sumOfSubjectPartsPerCycleThird += 1.0 / listOfSubjectProfessors.size();
                }
            }
            ProfessorStatsDTO dto = new ProfessorStatsDTO(professor, countFirstCycle, countSecondCycle, countThirdCycle, sumOfSubjectPartsPerCycleFirst, sumOfSubjectPartsPerCycleSecond, sumOfSubjectPartsPerCycleThird);
            professorStatsList.add(dto);
        }
        return new PageImpl<>(professorStatsList, pageRequest, mainListSize);
    }
}

