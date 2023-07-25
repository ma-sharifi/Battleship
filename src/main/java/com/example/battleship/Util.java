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
        Coordinate startCoordinate = Coordinate.fromLabel(shipDto.getStart());
        Ship ship = shipDto.getShipType().newInstance();
        ship.setDirection(shipDto.getDirection());
        LinkedList<Coordinate> coordinates = new LinkedList<>();
        ship.setCoordinates(coordinates);
        if (shipDto.getDirection() == Direction.HORIZONTAL) {//WE CAN ADD ALL SHIP OR WE CANT ADD WHOLE OF IT
            for (int col = startCoordinate.getColumn(); col < shipDto.getShipType().getLength() + startCoordinate.getColumn(); col++) {//-
                coordinates.add(new Coordinate(startCoordinate.getRow(), col));
            }
        } else {
            for (int row = startCoordinate.getRow(); row < shipDto.getShipType().getLength() + startCoordinate.getRow(); row++) {//-
                coordinates.add(new Coordinate(row, startCoordinate.getColumn()));
            }
        }

        return ship;
    }
}
