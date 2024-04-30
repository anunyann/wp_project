package mk.ukim.finki.akreditacii.repository;
import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    Page<Room> findAll(Pageable pageable);
    @Query("SELECT rm FROM Room rm " +
            "WHERE (:nameSearch is null OR rm.name ILIKE CONCAT('%', :nameSearch, '%')) " +
            "AND (:descriptionSearch is null OR COALESCE(rm.locationDescription, '') ILIKE CONCAT('%', :descriptionSearch, '%')) " +
            "AND (:participantsSearch is null OR rm.capacity = :participantsSearch) " +
            "AND (:typeSearch is null OR rm.type = :typeSearch)")
    Page<Room> findAllFiltered(
            @Param("nameSearch") String nameSearch,
            @Param("descriptionSearch") String descriptionSearch,
            @Param("participantsSearch") Long participantsSearch,
            @Param("typeSearch") RoomType typeSearch,
            Pageable pageable);

    Room findByName(String name);
}

