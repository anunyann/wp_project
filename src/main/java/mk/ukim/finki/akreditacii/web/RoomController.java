package mk.ukim.finki.akreditacii.web;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.akreditacii.model.room.Room;
import mk.ukim.finki.akreditacii.model.room.RoomType;
import mk.ukim.finki.akreditacii.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/admin/rooms")
    public String findAllSubjectsFiltered(Model model,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer results,
                                          @RequestParam(required = false) String nameSearch,
                                          @RequestParam(required = false) String locationDescriptionSearch,
                                          @RequestParam(required = false) String equipmentDescriptionSearch,
                                          @RequestParam(required = false) Long participantsSearch,
                                          @RequestParam(required = false) RoomType typeSearch

    ){
        Page<Room> roomPage;

        if (nameSearch == null && locationDescriptionSearch == null && equipmentDescriptionSearch==null && participantsSearch == null && typeSearch == null)  {
            roomPage = roomService.findAllWithPagination(pageNum,results);

        } else {
            roomPage =  roomService.findAllWithPaginationFiltered(pageNum,results,nameSearch,locationDescriptionSearch,equipmentDescriptionSearch,participantsSearch,typeSearch);
            model.addAttribute("nameSearch", nameSearch);
            model.addAttribute("locationDescriptionSearch", locationDescriptionSearch);
            model.addAttribute("participantsSearch", participantsSearch);
            model.addAttribute("typeSearch",typeSearch);

        }
        model.addAttribute("types",RoomType.values());
        model.addAttribute("rooms", roomPage);
        return "room/room.html";
    }
    @PostMapping("/admin/rooms/delete/{name}")
    public String deleteProduct(@PathVariable String name){
        this.roomService.delete(name);
        return "redirect:/admin/rooms";

    }
    @GetMapping("/admin/rooms/edit/{name}")
    public String editProductPage(@PathVariable String name,Model model) {
        model.addAttribute("room", roomService.findByName(name));
        model.addAttribute("types",RoomType.values());
        return "room/edit_room.html";
    }
    @PostMapping("/admin/rooms/edit/{name}")
    public String editProduct(
            @PathVariable String name,
            @RequestParam String newName,
            @RequestParam String locationDescription,
            @RequestParam String equipmentDescription,
            @RequestParam RoomType type,
            @RequestParam Long capacity) {
        this.roomService.update(name,newName,locationDescription,equipmentDescription,type,capacity);
        return "redirect:/admin/rooms";
    }
    @GetMapping("/admin/rooms/add")
    public String addProductPage(Model model) {
        Room room = new Room();
        model.addAttribute("types",RoomType.values());
        model.addAttribute("room", room);
        return "room/add_room.html";
    }
    @GetMapping("/admin/rooms/import")
    public String ImportPage(Model model) {
        return "room/import.html";
    }
    @PostMapping("/admin/rooms/add")
    public String saveProduct(
            @RequestParam String name,
            @RequestParam String locationDescription,
            @RequestParam String equipmentDescription,
            @RequestParam RoomType type,
            @RequestParam Long capacity) {
        this.roomService.create(name,locationDescription,equipmentDescription,type,capacity);
        return "redirect:/admin/rooms";
    }
    @GetMapping("/admin/rooms/download")
    public ResponseEntity<byte[]> exportRooms(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "100") Integer results,
            @RequestParam(required = false) String nameSearch,
            @RequestParam(required = false) String locationDescriptionSearch,
            @RequestParam(required = false) String equipmentDescriptionSearch,
            @RequestParam(required = false) Long participantsSearch,
            @RequestParam(required = false) RoomType typeSearch) throws IOException {

        Page<Room> roomsPage;
        if (nameSearch == null && locationDescriptionSearch == null && equipmentDescriptionSearch == null && participantsSearch == null && typeSearch == null) {
            roomsPage = roomService.findAllWithPagination(pageNum, results);
        } else {
            roomsPage = roomService.findAllWithPaginationFiltered(pageNum, results, nameSearch, locationDescriptionSearch,equipmentDescriptionSearch, participantsSearch, typeSearch);
        }

        List<Room> filteredRooms = roomsPage.getContent();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Pagination,Name,Location Description,Equipment Description,Type,Capacity\n".getBytes(StandardCharsets.UTF_8));

        int pagination = (pageNum - 1) * results + 1;

        for (Room room : filteredRooms) {
            String name = Optional.ofNullable(room.getName()).orElse("").replace("\"", "\"\"");
            String locationDesc = Optional.ofNullable(room.getLocationDescription()).orElse("").replace("\"", "\"\"");
            String equipmentDesc = Optional.ofNullable(room.getEquipmentDescription()).orElse("").replace("\"", "\"\"");
            String type = room.getType() == null ? "" : room.getType().toString();
            Long capacity = room.getCapacity() == null ? 0L : room.getCapacity();

            String rowData = String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",%d\n",
                    pagination++,
                    name,
                    locationDesc,
                    equipmentDesc,
                    type,
                    capacity);
            outputStream.write(rowData.getBytes(StandardCharsets.UTF_8));
        }

        byte[] csvBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"rooms.csv\"");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csvBytes.length)
                .body(csvBytes);
    }



}