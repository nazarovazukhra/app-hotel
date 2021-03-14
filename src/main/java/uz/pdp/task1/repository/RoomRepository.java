package uz.pdp.task1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.task1.entity.Room;


@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByNumberAndHotelId(String number, Integer hotel_id);


       /*  NATIVE  QUERY
        @Query(value = "select * from room where hotel_id=:hotelId", nativeQuery = true)
        Page<Room> getRoomsByHotelId(Integer hotelId, Pageable pageable);
      */


    Page<Room> findRoomsByHotel_Id(Integer hotel_id, Pageable pageable);
}
