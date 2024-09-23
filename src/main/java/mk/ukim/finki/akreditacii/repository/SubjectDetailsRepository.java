package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.subject.SubjectDetails;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectAllocationStatsDTO;
import mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectDetailsRepository extends JpaSpecificationRepository<SubjectDetails, String> {

    Page<SubjectDetails> findAll(Pageable pageable);

    @Query("SELECT sd FROM SubjectDetails sd " +
            "WHERE (:nameSearch is null OR sd.subject.name ILIKE %:nameSearch%) " +
            "AND (:accreditationYear is null OR sd.accreditation.year = :accreditationYear)")
    Page<SubjectDetails> findAllFiltered(
            @Param("nameSearch") String nameSearch,
            @Param("accreditationYear") String accreditationYear,
            Pageable pageable
    );

    @Query("SELECT new mk.ukim.finki.akreditacii.model.subject.dto.SubjectAllocationStatsDTO(" +
            "sas.subject.mainSubject.id, " +
            "COUNT(sas.id), " +
            "AVG(sas.numberOfFirstTimeStudents), " +
            "AVG(sas.numberOfReEnrollmentStudents)) " +
            "FROM SubjectAllocationStats sas " +
            "GROUP BY sas.subject.mainSubject.id")
    List<SubjectAllocationStatsDTO> getSubjectAllocationStatsDTOList();

    @Query("SELECT new mk.ukim.finki.akreditacii.model.subject.dto.SubjectNameAndCodeDTO(" +
            "s.id, " +
            "s.name) " +
            "FROM Subject s ")
    List<SubjectNameAndCodeDTO> findAllNameAndCode();
}
