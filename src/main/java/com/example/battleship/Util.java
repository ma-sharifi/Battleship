package com.example.battleship;

import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ship.Direction;
import com.example.battleship.model.ship.Ship;

import java.util.LinkedList;

/**
 * @author Mahdi Sharifi
 */
public interface Util {

    static Ship shipDtoToShip(ShipDto shipDto) {
        Coordinate startCoordinate = new Coordinate(shipDto.start());
        Ship ship = shipDto.type().newInstance();
        ship.setDirection(shipDto.direction());
        LinkedList<Coordinate> coordinates = new LinkedList<>();
        ship.setCoordinates(coordinates);
        if (shipDto.direction() == Direction.HORIZONTAL) {//WE CAN ADD ALL SHIP OR WE CANT ADD WHOLE OF IT
            for (int col = startCoordinate.getColumn(); col < shipDto.type().length() + startCoordinate.getColumn(); col++) {//-
                coordinates.add(new Coordinate(startCoordinate.getRow(), col));
            }
        } else {
            for (int row = startCoordinate.getRow(); row < shipDto.type().length() + startCoordinate.getRow(); row++) {//-
                coordinates.add(new Coordinate(row, startCoordinate.getColumn()));
            }
        }

        return ship;
    }
}
