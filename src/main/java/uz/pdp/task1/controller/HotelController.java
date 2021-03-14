package uz.pdp.task1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.entity.Hotel;
import uz.pdp.task1.repository.HotelRepository;

import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {


    final HotelRepository hotelRepository;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    @PostMapping
    public String add(@RequestBody Hotel hotel) {

        Hotel newHotel = new Hotel();
        newHotel.setName(hotel.getName());
        newHotel.setAddress(hotel.getAddress());

        boolean existsByNameAndAddress = hotelRepository.existsByNameAndAddress(hotel.getName(), hotel.getAddress());
        if (existsByNameAndAddress)
            return "Such hotel exists in this address";
        hotelRepository.save(newHotel);
        return "Hotel added";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Integer id) {

        boolean exists = hotelRepository.existsById(id);
        if (exists) {
            hotelRepository.deleteById(id);
            return "Hotel deleted";
        }
        return "Such hotel not found";
    }


    @PutMapping("/{id}")
    public String editById(@PathVariable Integer id, @RequestBody Hotel hotel) {

        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (optionalHotel.isPresent()) {
            Hotel editing = optionalHotel.get();
            editing.setName(hotel.getName());
            editing.setAddress(hotel.getAddress());
            hotelRepository.save(editing);
            return "Hotel edited";
        }
        return "Such hotel not found";
    }


    @GetMapping("/{id}")
    public Hotel getById(@PathVariable Integer id) {

        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.orElse(null);
    }


    @GetMapping
    public Page<Hotel> getHotelList(@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
        return hotelPage;


    }
}
