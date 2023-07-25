package com.example.battleship.controller.dto;


import com.example.battleship.model.ship.Direction;
import com.example.battleship.model.ship.ShipType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "It presents a ship just by 3 values of direction, type, and start Coordinate of it. Since we know the length and direction of the ship, we can calculate the other coordinates of a ship on board.")
public class ShipDto {

    private final ShipType shipType;
    private Direction direction;
    @Size(min = 2, max = 2)
    private String start;// Label of ship: A1 , I2 , C5

    public ShipDto(ShipType shipType) {
        this.shipType = shipType;
    }

    public ShipDto( ShipType shipType,Direction direction, String start) {
        this.shipType = shipType;
        this.direction = direction;
        this.start = start;
    }
}
