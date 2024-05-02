package mk.ukim.finki.akreditacii.web.api;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
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

    @PostMapping("/api/rooms/import")
    public void importRooms(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        if (file.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("No file was provided for the import.");
            } catch (IOException e) {
                e.printStackTrace(); // Log this error appropriately
            }
            return;
        }

        List<Room> invalidRooms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim().replaceAll("^\"|\"$", ""); // Trim and remove surrounding quotes
                }

                try {
                    Room room = new Room();
                    room.setName(data[0]);
                    room.setLocationDescription(data[1]);
                    room.setEquipmentDescription(data[2]);
                    room.setType(RoomType.valueOf(data[3])); // This might throw IllegalArgumentException
                    room.setCapacity(Long.parseLong(data[4]));
                    roomService.create(room.getName(), room.getLocationDescription(), room.getEquipmentDescription(), room.getType(), room.getCapacity());
                } catch (IllegalArgumentException e) {
                    // Log the exception or handle it as appropriate
                    invalidRooms.add(new Room(data[0], data[1], data[2], null, Long.parseLong(data[4])));
                }
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error reading from the file.");
            } catch (IOException ex) {
                ex.printStackTrace(); // Log this error appropriately
            }
            return;
        }

        if (!invalidRooms.isEmpty()) {
            String fileName = "invalid_rooms.tsv";
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                for (Room room : invalidRooms) {
                    String line = String.join("\t", room.getName(), room.getLocationDescription(), room.getEquipmentDescription(),
                            (room.getType() == null ? "INVALID_TYPE" : room.getType().toString()), room.getCapacity().toString());
                    outputStream.write((line + "\n").getBytes());
                }
                outputStream.flush();
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter().write("All rooms have been successfully imported.");
            } catch (IOException e) {
                e.printStackTrace(); // Log this error appropriately
            }
        }
    }
}