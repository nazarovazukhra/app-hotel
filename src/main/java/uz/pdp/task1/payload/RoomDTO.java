package uz.pdp.task1.payload;

import lombok.Data;

@Data
public class RoomDTO {

    private String number;
    private Integer floor;
    private Double size;
    private Integer hotelId;
}
