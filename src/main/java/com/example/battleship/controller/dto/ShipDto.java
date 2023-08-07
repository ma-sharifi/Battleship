package com.example.battleship.controller.dto;


import com.example.battleship.model.ship.Direction;
import com.example.battleship.model.ship.ShipType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "It presents a ship just by 3 values of direction, type, and start Coordinate of it. Since we know the length and direction of the ship, we can calculate the other coordinates of a ship on board.")
public record ShipDto(ShipType type,Direction direction,String start) {}
