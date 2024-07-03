package mk.ukim.finki.akreditacii.web.api;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
import mk.ukim.finki.akreditacii.repository.ImportRepository;
import mk.ukim.finki.akreditacii.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestRoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ImportRepository importRepository;

    @PostMapping("/api/rooms/import")
    public void importRooms(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {

            List<Room> rooms = importRepository.readRooms(file, Room.class);


            List<Room> importedRooms = roomService.importData(rooms);


            String fileName = "imported_rooms.tsv";
            response.setContentType("text/tab-separated-values");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                importRepository.writeRooms(Room.class, importedRooms, outputStream);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to process the file", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during the import operation", e);
        }

    }
}