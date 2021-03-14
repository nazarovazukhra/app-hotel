package uz.pdp.task1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.entity.Hotel;
import uz.pdp.task1.entity.Room;
import uz.pdp.task1.payload.RoomDTO;
import uz.pdp.task1.repository.HotelRepository;
import uz.pdp.task1.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {


    final RoomRepository roomRepository;
    final HotelRepository hotelRepository;

    public RoomController(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }


    @PostMapping
    public String add(@RequestBody RoomDTO roomDTO) {

        Room room = new Room();
        room.setNumber(roomDTO.getNumber());
        room.setFloor(roomDTO.getFloor());
        room.setSize(roomDTO.getSize());

        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDTO.getHotelId());
        if (optionalHotel.isPresent()) {

            Hotel hotel = optionalHotel.get();
            room.setHotel(hotel);

            boolean existsByNumberAndHotelId = roomRepository.existsByNumberAndHotelId(roomDTO.getNumber(), roomDTO.getHotelId());

            if (existsByNumberAndHotelId)
                return "Such room exists in this hotel";

            roomRepository.save(room);
            return "Room added";
        }
        return "Such hotel not found";
    }


    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Integer id) {

        boolean exists = roomRepository.existsById(id);
        if (exists) {
            roomRepository.deleteById(id);
            return "Room deleted";
        }
        return "Such room not found";

    }


    @PutMapping("/{id}")
    public String editById(@PathVariable Integer id, @RequestBody RoomDTO roomDTO) {

        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            Room editing = optionalRoom.get();
            editing.setNumber(roomDTO.getNumber());
            editing.setFloor(roomDTO.getFloor());
            editing.setSize(roomDTO.getSize());


            Optional<Hotel> optionalHotel = hotelRepository.findById(roomDTO.getHotelId());
            if (optionalHotel.isPresent()) {
                Hotel hotel = optionalHotel.get();

                editing.setHotel(hotel);
                roomRepository.save(editing);
                return "Room edited";
            } else {
                return "Such hotel not found";
            }
        }
        return "Such room not found";
    }


    @GetMapping("/{id}")
    public Room getById(@PathVariable Integer id) {

        Optional<Room> optionalRoom = roomRepository.findById(id);
        return optionalRoom.orElse(null);
    }


    @GetMapping
    public Page<Room> getRoomList(@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 3);
        return roomRepository.findAll(pageable);

    }

    @GetMapping("/hotel/{id}")
    public Page<Room> roomListByHotelId(@PathVariable Integer id, @RequestParam int page) {

        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (optionalHotel.isPresent()) {

            Pageable pageable = PageRequest.of(page, 3);
            Page<Room> roomsByHotelId = roomRepository.findRoomsByHotel_Id(id, pageable);
            return roomsByHotelId;
        }
        return null;
    }
}
