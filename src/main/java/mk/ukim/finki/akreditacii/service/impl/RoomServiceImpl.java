package mk.ukim.finki.akreditacii.service.impl;

import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
import mk.ukim.finki.akreditacii.model.exceptions.InvalidRoomIdException;
import mk.ukim.finki.akreditacii.repository.RoomRepository;
import mk.ukim.finki.akreditacii.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Page<Room> findAllWithPagination(Integer pageNum, Integer results) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);
        return roomRepository.findAll(pageRequest);
    }
    @Override
    public Page<Room> findAllWithPaginationFiltered(Integer pageNum, Integer results,
                                                    String nameSearch,
                                                    String descriptionSearch,
                                                    Long participantsSearch,
                                                    RoomType typeSearch) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, results);


        return roomRepository.findAllFiltered(nameSearch,descriptionSearch,participantsSearch,typeSearch,pageRequest);

    }

    @Override
    public Room findByName(String name) {
        return this.roomRepository.findByName(name);
    }
    @Override
    public Room create( String name, String locationDescription, String equipmentDescription, RoomType type, Long capacity) {
        Room room = new Room( name, locationDescription,equipmentDescription,type,capacity);
        return this.roomRepository.save(room);
    }

    @Override
    public Room update( String name, String locationDescription, String equipmentDescription, RoomType type, Long capacity) {
        Room room = findByName(name);
        room.setName(name);
        room.setLocationDescription(locationDescription);
        room.setEquipmentDescription(equipmentDescription);
        room.setType(type);
        room.setCapacity(capacity);
        return this.roomRepository.save(room);

    }

    @Override
    public Room delete(String name) {
        Room room = findByName(name);
        this.roomRepository.delete(room);
        return room;
    }

}
