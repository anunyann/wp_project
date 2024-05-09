package mk.ukim.finki.akreditacii.repository;
import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RoomRepository extends JpaSpecificationRepository<Room,String> {

    Page<Room> findAll(Specification<Room> filter,Pageable pageable);
    @Query("SELECT rm FROM Room rm " +
            "WHERE (:nameSearch is null OR rm.name ILIKE CONCAT('%', :nameSearch, '%')) " +
            "AND (:locationDescriptionSearch is null OR COALESCE(rm.locationDescription, '') ILIKE CONCAT('%', :locationDescriptionSearch, '%')) " +
            "AND (:equipmentDescriptionSearch is null OR COALESCE(rm.equipmentDescription, '') ILIKE CONCAT('%', :equipmentDescriptionSearch, '%')) " +
            "AND (:participantsSearch is null OR rm.capacity = :participantsSearch) " +
            "AND (:typeSearch is null OR rm.type = :typeSearch)")
    Page<Room> findAllFiltered(
            @Param("nameSearch") String nameSearch,
            @Param("locationDescriptionSearch") String locationDescriptionSearch,
            @Param("equipmentDescriptionSearch") String equipmentDescriptionSearch,
            @Param("participantsSearch") Long participantsSearch,
            @Param("typeSearch") RoomType typeSearch,
            Pageable pageable);

    Room findByName(String name);
}

