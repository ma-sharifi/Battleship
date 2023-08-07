package com.example.battleship.service;

import com.example.battleship.exception.*;
import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ship.Ship;
import com.example.battleship.model.ship.ShipType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahdi Sharifi
 * IT check validation of game input and check gameplay constraints
 */

@Service
public class ValidatorService {

    public void validate(List<Ship> fleet) {
        checkDuplicatedShipAdded(fleet);
        checkNumberOfShip(fleet);
        shipIsOutOfGrid(fleet);
    }

    /**
     * It Checks if 2 ships have overlap on each other
     */
    public boolean checkShipOverlap(Cell[][] board, Ship ship) {
        ship.getCoordinates().forEach(coordinate -> {
            if (board[coordinate.getRow()][coordinate.getColumn()].isShip())
                throw new ViolateOverlapException("You are trying to place ship "+ship.type()+" with orientation "+ship.getDirection()
                        +". In label '" + coordinate.toLabel() + "' it overlapped with ship " +
                        ShipType.getTypeByShipId(board[coordinate.getRow()][coordinate.getColumn()].getShipId())+" that is already placed.");
        });
        return true;
    }

    /**
     * It checks if we have more than one type of ships in our fleet.
     */
    private void checkDuplicatedShipAdded(List<Ship> ships) {
        List<String> shipTypes = new ArrayList<>();
        for (Ship ship: ships) {
            var shipType = ship.type().id().toString();
            if (shipTypes.contains(shipType)) {
                throw new DuplicateShipException(shipType+" ;Coordinates: "+ship.getCoordinates().stream()
                        .map(Coordinate::toLabel).toList());
            }
            shipTypes.add(shipType);
        }
    }

    private void checkNumberOfShip(List<Ship> fleet) {
        int expectedFleetSize = ShipType.values().length;
        int realFleetSize = fleet.size();
        if (realFleetSize != expectedFleetSize) {
            throw new ViolateNumberOfShipException(String.format(" %d, actual: %d", expectedFleetSize, realFleetSize));
        }
    }

    private void shipIsOutOfGrid(List<Ship> fleet) {
        for (Ship ship: fleet) {
            boolean isOutOfGrid = ship.getCoordinates().stream().anyMatch(this::coordinateIsOutOfBoard);
            if (isOutOfGrid) {
                throw new OutOfBoardException(ship.type().name());
            }
        }

    }

    public boolean coordinateIsOutOfBoard(Coordinate coordinate) {
        return coordinate.getRow() < 0 ||
                coordinate.getColumn() < 0 ||
                coordinate.getRow() >= 10 ||
                coordinate.getColumn() >= 10;
    }
}
