package com.example.battleship.service;

import com.example.battleship.exception.DuplicateShipException;
import com.example.battleship.exception.OutOfBoardException;
import com.example.battleship.exception.ViolateNumberOfShipException;
import com.example.battleship.exception.ViolateOverlapException;
import com.example.battleship.exception.errorcode.ErrorCode;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ship.Ship;
import com.example.battleship.model.ship.ShipType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mahdi Sharifi
 * IT check validation of game input and check gameplay constraints
 */

@Service
@Slf4j
public class ValidatorService {

    public void validate(List<Ship> fleet) {
        checkDuplicatedShipAdded(fleet);
        checkNumberOfShip(fleet);
        shipIsOutOfGrid(fleet);
        checkShipOverlap(fleet);
    }

    /**
     * It checks if we have more than one type of ships in our fleet.
     */
    private void checkDuplicatedShipAdded(List<Ship> fleet) {
        var currentFleetShipTypeCount = fleet.stream()
                .map(Ship::type)//to type
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));//find duplicated

        currentFleetShipTypeCount.forEach((key, value) -> {
            if (value > 1) {
                throw new DuplicateShipException(key.name());
            }
        });
        List<String> shipTypes = new ArrayList<>();

        for (Ship ship : fleet) {
            var shipType = ship.type().id().toString();
            if (shipTypes.contains(shipType)) {
                throw new DuplicateShipException(shipType + " ;Coordinates: " + ship.getCoordinates().stream()
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
        for (Ship ship : fleet) {
            boolean isOutOfGrid = ship.getCoordinates().stream().anyMatch(this::coordinateIsOutOfBoard);
            if (isOutOfGrid) {
                throw new OutOfBoardException("Ship is out of board bound! Ship type: "+ship.type().name(), ErrorCode.OUT_OF_BOARD_ERROR);
            }
        }
    }

    /**
     * I prefer to use above method, since its faster than this method.
     */
    private void checkShipOverlap(List<Ship> fleet) {
        Map<Coordinate, Long> coordinateCount = fleet.stream()
                .map(Ship::getCoordinates)//to List<List<Coordinate>>
                .flatMap(Collection::stream)//to List<Coordinate>
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));//count the number od each coordinate

        List<Coordinate> duplicatedLabels = coordinateCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)//Find duplicate coordinate
                .map(Map.Entry::getKey).toList();// map to List of Coordinates

        if (!duplicatedLabels.isEmpty()) { // ships overlapped? Create an understandable message for a client
            var message = new StringBuilder();
            for (Ship ship : fleet) {
                List<String> common = ship.getCoordinates().stream()
                        .filter(duplicatedLabels::contains)// find labeled in common with duplicatedLabels list
                        .map(Coordinate::toLabel)//to label
                        .toList();
                if (!common.isEmpty())
                    message.append("\nShip '").append(ShipType.getTypeByShipId(ship.getId())).append("' is ").append(ship.getDirection())
                            .append(" ,overlapped coordinates found: ").append(common);
            }
            throw new ViolateOverlapException(duplicatedLabels.size() + " coordinates overlapped!" + message + "\nCheck your fleet for overlapped ships");
        }
    }

    public boolean coordinateIsOutOfBoard(Coordinate coordinate) {
        return coordinate.getRow() < 0 ||
                coordinate.getColumn() < 0 ||
                coordinate.getRow() >= 10 ||
                coordinate.getColumn() >= 10;
    }
}
