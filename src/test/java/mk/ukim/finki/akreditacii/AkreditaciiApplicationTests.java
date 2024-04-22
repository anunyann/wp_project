package mk.ukim.finki.akreditacii;

import mk.ukim.finki.akreditacii.model.SemesterType;
import mk.ukim.finki.akreditacii.model.StudyCycle;
import mk.ukim.finki.akreditacii.model.accreditation.Accreditation;
import mk.ukim.finki.akreditacii.model.professor.AcademicTitle;
import mk.ukim.finki.akreditacii.model.professor.Professor;
import mk.ukim.finki.akreditacii.model.professor.ProfessorTitle;
import mk.ukim.finki.akreditacii.model.study_program.StudyProgram;
import mk.ukim.finki.akreditacii.model.subject.*;
import mk.ukim.finki.akreditacii.model.view_model.ProfessorStatsDTO;
import mk.ukim.finki.akreditacii.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AkreditaciiApplicationTests {
    @Autowired
    ProfessorService professorService;

    public static List<ProfessorStatsDTO> getStatsForAllProfessorsWithPagination(List<Professor> professorList, List<Subject> subjectList,
                                                                                 List<SubjectDetails> subjectDetailsList, List<StudyProgramSubject> studyProgramSubjectList,
                                                                                 List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList) {
        int countFirstCycle, countSecondCycle, countThirdCycle;

        List<ProfessorStatsDTO> professorStatsList = new ArrayList<>();

        for (Professor professor : professorList) {
            List<StudyProgramSubjectProfessor> collect = studyProgramSubjectProfessorList.stream().filter(s -> s.getProfessor().equals(professor)).toList();
            countFirstCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.UNDERGRADUATE)).count();
            countSecondCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.MASTER)).count();
            countThirdCycle = (int) collect.stream().filter(t -> t.getStudyProgramSubject().getSubject().getCycle().equals(StudyCycle.PHD)).count();
            ProfessorStatsDTO dto = new ProfessorStatsDTO(professor, countFirstCycle, countSecondCycle, countThirdCycle);
            professorStatsList.add(dto);
        }

        return professorStatsList;

//        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
//        List<ProfessorStatsDTO> paginatedList = professorStatsList.subList();
        // page.getContent().add()
    }

    public List<ProfessorStatsDTO> getStatsForAllProfessorsWithPaginationAndFilters(List<Professor> professorList, List<Subject> subjectList,
                                                                                    List<SubjectDetails> subjectDetailsList, List<StudyProgramSubject> studyProgramSubjectList,
                                                                                    List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList) {


        List<ProfessorStatsDTO> professorStatsList = new ArrayList<>();
        int countFirstCycle, countSecondCycle, countThirdCycle;
        double sumOfSubjectPartsPerCycleFirst = 0, sumOfSubjectPartsPerCycleSecond = 0, sumOfSubjectPartsPerCycleThird = 0;
        List<StudyProgramSubjectProfessor> firstCycle, secondCycle, thirdCycle;

        for (int i = 0; i < professorList.size(); i++) {
            var professor = professorList.get(i);
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
                } else if (subject.getCycle().equals(StudyCycle.MASTER)) {
                    var subject2 = subject.getSubject();
                    secondCycle = studyProgramSubjectProfessorList.stream().filter(p -> p.getStudyProgramSubject().getSubject().getSubject().equals(subject2)).toList();
                    System.out.println(secondCycle.size());
                    sumOfSubjectPartsPerCycleSecond += 1.0 / secondCycle.size();
                } else {
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
        return professorStatsList;
    }

    @Test
    void contextLoads() {
    }

    List<Professor> professorList;
    List<StudyProgramSubjectProfessor> studyProgramSubjectProfessorList;
    List<Subject> subjectList;
    List<SubjectDetails> subjectDetails;
    List<StudyProgramSubject> studyProgramSubjectList;

    @BeforeEach
    void setup() {
        Professor professor1 = new Professor("1", "Aleksandar", "Stojmenski", ProfessorTitle.PROFESSOR);
        Professor professor2 = new Professor("2", "Stojan", "Stojanovski", ProfessorTitle.PROFESSOR);

        Subject subject1 = new Subject("1", "Kalkulus", "", SemesterType.WINTER, 3, 2, 1);
        Subject subject2 = new Subject("2", "SP", "", SemesterType.WINTER, 3, 2, 1);
        Subject subject3 = new Subject("3", "Nz", "", SemesterType.SUMMER, 3, 2, 0);

        SubjectDetails details1 = new SubjectDetails("1", "1", subject1, true, "Kalkulus", (short) 5, 6.0F,
                StudyCycle.UNDERGRADUATE, "mk", "A", "A", "A", "A", "A", "A",
                new Accreditation(), new SubjectObligationDuration(), new SubjectDependencies(),
                new SubjectGrading(), new SubjectBibliography(), new ArrayList<>());
        SubjectDetails details2 = new SubjectDetails("1", "1", subject2, true, "SP", (short) 5, 6.0F,
                StudyCycle.MASTER, "mk", "A", "A", "A", "A", "A", "A",
                new Accreditation(), new SubjectObligationDuration(), new SubjectDependencies(),
                new SubjectGrading(), new SubjectBibliography(), new ArrayList<>());
        SubjectDetails details3 = new SubjectDetails("1", "1", subject3, true, "Nz", (short) 5, 6.0F,
                StudyCycle.PHD, "mk", "A", "A", "A", "A", "A", "A",
                new Accreditation(), new SubjectObligationDuration(), new SubjectDependencies(),
                new SubjectGrading(), new SubjectBibliography(), new ArrayList<>());

        //Kalkulus
        StudyProgramSubject studyProgramSubject1 = new StudyProgramSubject("1", details1, new StudyProgram(), true, (short) 1,
                6.0F, "A", "A");
        // SP
        StudyProgramSubject studyProgramSubject2 = new StudyProgramSubject("2", details2, new StudyProgram(), true, (short) 1,
                6.0F, "A", "A");
        // AA
        StudyProgramSubject studyProgramSubject3 = new StudyProgramSubject("3", details3, new StudyProgram(), true, (short) 1,
                6.0F, "A", "A");

        professorList = new ArrayList<>(Arrays.asList(professor1, professor2));
        List<Subject> subjectList = new ArrayList<>(Arrays.asList(subject1, subject2, subject3));
        List<SubjectDetails> subjectDetails = new ArrayList<>(Arrays.asList(details1, details2, details3));
        List<StudyProgramSubject> studyProgramSubjectList = new ArrayList<>(Arrays.asList(studyProgramSubject1, studyProgramSubject2, studyProgramSubject3));

        // stojmenski gi drzhi kalkulus i sp od dodiplomski i magisterski
        StudyProgramSubjectProfessor studyProgramSubjectProfessor1 = new StudyProgramSubjectProfessor("1", studyProgramSubject1, professor1, 6F);
        StudyProgramSubjectProfessor studyProgramSubjectProfessor2 = new StudyProgramSubjectProfessor("2", studyProgramSubject2, professor1, 6F);
        StudyProgramSubjectProfessor studyProgramSubjectProfessor3 = new StudyProgramSubjectProfessor("3", studyProgramSubject3, professor1, 6F);
        StudyProgramSubjectProfessor studyProgramSubjectProfessor4 = new StudyProgramSubjectProfessor("4", studyProgramSubject1, professor2, 6F);
        StudyProgramSubjectProfessor studyProgramSubjectProfessor5 = new StudyProgramSubjectProfessor("5", studyProgramSubject2, professor2, 6F);
        StudyProgramSubjectProfessor studyProgramSubjectProfessor6 = new StudyProgramSubjectProfessor("6", studyProgramSubject3, professor1, 6F);

        studyProgramSubjectProfessorList = new ArrayList<>(
                Arrays.asList(studyProgramSubjectProfessor1, studyProgramSubjectProfessor2, studyProgramSubjectProfessor3, studyProgramSubjectProfessor4, studyProgramSubjectProfessor5));
    }

    @Test
    void test1() {
        List<ProfessorStatsDTO> res = getStatsForAllProfessorsWithPaginationAndFilters(professorList, subjectList, subjectDetails, studyProgramSubjectList, studyProgramSubjectProfessorList);
        ProfessorStatsDTO result = res.get(0);
        System.out.println(result);
        //assertArrayEquals(new Integer[]{result.getFirstCycleTotal(), result.getSecondCycleTotal(), result.getThirdCycleTotal()}, new Integer[]{2, 0, 1});
        assertArrayEquals(new Double[]{0.5, 0.5, 1.0}, new Double[]{result.getSumPerCycleFirst(), result.getSumPerCycleSecond(), result.getSumPerCycleThird()});
    }
}
