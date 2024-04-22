package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidId;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubject;
import mk.ukim.finki.akreditacii.model.subject.StudyProgramSubjectProfessor;
import mk.ukim.finki.akreditacii.model.view_model.ProfessorStatsDTO;
import mk.ukim.finki.akreditacii.repository.StudyProgramSubjectProfessorRepository;
import mk.ukim.finki.akreditacii.repository.professor.ProfessorRepository;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (stringSearch != null && filteredTitle != null) {
            professorList = professorRepository.findAllFilteredWithoutPaging(stringSearch,
                    !filteredTitle.isEmpty() ? ProfessorTitle.valueOf(filteredTitle) : null);
        } else {
            professorList = professorRepository.findAll();
        }
        List<ProfessorStatsDTO> professorStatsList = new ArrayList<>();

        int countFirstCycle, countSecondCycle, countThirdCycle;
        List<StudyProgramSubjectProfessor> firstCycle, secondCycle, thirdCycle;

        int mainListSize = professorList.size();
        int startIndex = (pageNum - 1) * results;
        int endIndex = Math.min(startIndex + results, professorList.size());


        if (endIndex < startIndex || startIndex < 0) {
            startIndex = 0;
            endIndex = Math.min(results, mainListSize); // Get first 'results' elements if invalid startIndex
        } else if (endIndex > mainListSize) {
            endIndex = mainListSize; // Ensure endIndex doesn't exceed the list size
        }
        professorList = professorList.subList(startIndex, endIndex);


        for (int i = 0; i < professorList.size(); i++) {
            double sumOfSubjectPartsPerCycleFirst = 0, sumOfSubjectPartsPerCycleSecond = 0, sumOfSubjectPartsPerCycleThird = 0;
            var professor = professorList.get(i);
            List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList = studyProgramSubjectProfessorRepository.findAllByProfessorId(professor.getId());

            // predmeti sto gi drzi profesorot
            List<StudyProgramSubjectProfessor> collect = studyProgramSubjectProfessorList.stream().filter(s -> s.getProfessor().equals(professor)).toList();

            // iteriranje niz predmetite sto gi drzi profesorot
            for (var object : collect) {
                // predmet sto go drzi profesorot
                var subject = object.getStudyProgramSubject().getSubject();
                if (subject.getCycle().equals(StudyCycle.UNDERGRADUATE)) {
                    var subject2 = subject.getSubject();
                    firstCycle = studyProgramSubjectProfessorList.stream().filter(p -> p.getStudyProgramSubject().getSubject().getSubject().equals(subject2)).toList();
                    System.out.println(firstCycle.size());
                    sumOfSubjectPartsPerCycleFirst += 1.0 / firstCycle.size();
                }
                if (subject.getCycle().equals(StudyCycle.MASTER)) {
                    var subject2 = subject.getSubject();
                    secondCycle = studyProgramSubjectProfessorList.stream().filter(p -> p.getStudyProgramSubject().getSubject().getSubject().equals(subject2)).toList();
                    System.out.println(secondCycle.size());
                    sumOfSubjectPartsPerCycleSecond += 1.0 / secondCycle.size();
                }
                if (subject.getCycle().equals(StudyCycle.PHD)) {
                    var subject2 = subject.getSubject();
                    thirdCycle = studyProgramSubjectProfessorList.stream().filter(p -> p.getStudyProgramSubject().getSubject().getSubject().equals(subject2)).toList();
                    System.out.println(thirdCycle.size());
                    sumOfSubjectPartsPerCycleThird += 1.0 / thirdCycle.size();
                }
            }

            // nadole e okej
            countFirstCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.UNDERGRADUATE)).count();
            countSecondCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.MASTER)).count();
            countThirdCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.PHD)).count();


            ProfessorStatsDTO dto = new ProfessorStatsDTO(professor, countFirstCycle, countSecondCycle, countThirdCycle, sumOfSubjectPartsPerCycleFirst, sumOfSubjectPartsPerCycleSecond, sumOfSubjectPartsPerCycleThird);
            professorStatsList.add(dto);
        }

        return new PageImpl<>(professorStatsList, pageRequest, mainListSize);
    }
}

