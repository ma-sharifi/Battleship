package com.example.battleship.service;

import com.example.battleship.exception.*;
import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ship.Direction;
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
                throw new ViolateOverlapException("Label: " + coordinate.isHit() + " ;Overlap: " + board[coordinate.getRow()][coordinate.getColumn()]);//DONT OVERLAP
        });
        return true;
    }

    /**
     * It Checks if 2 ships have touched each other. It means that they are neighbors without any cells between.
     */
    public boolean checkShipDontTouchEachOther(Cell[][] cells, Ship ship) {
        var startShipCoordinate = ship.getCoordinates().getFirst();
        var endShipCoordinate = ship.getCoordinates().getLast();

        int aboveNeighborRow = Math.max(startShipCoordinate.getRow() - 1, 0);
        int bottomNeighborRow = Math.min(endShipCoordinate.getRow() + 1, cells[0].length - 1);

        int leftNeighborCol = Math.max(startShipCoordinate.getColumn() - 1, 0);
        int rightNeighborCol = Math.min(endShipCoordinate.getColumn() + 1, cells.length - 1);

        if (ship.getDirection() == Direction.VERTICAL) {
            //Check top and bottom cell don't touch by other ship
            if (cells[startShipCoordinate.getRow()][leftNeighborCol].isShip() || cells[startShipCoordinate.getRow()][rightNeighborCol].isShip())
                throw new ViolateTouchException("row: " + aboveNeighborRow + " ;col: " + leftNeighborCol + " ;Touch: " + cells[aboveNeighborRow][leftNeighborCol]);// Check the previous Col and the next col there was not ship in a vertical ship

            checkLeftColumnAndRightColumnDontTouchByNewShip(cells, aboveNeighborRow, leftNeighborCol, bottomNeighborRow, rightNeighborCol);
        } else {
            //Check left and right cell dont touch by other ship
            if (cells[aboveNeighborRow][startShipCoordinate.getColumn()].isShip() || cells[bottomNeighborRow][startShipCoordinate.getColumn()].isShip())
                throw new ViolateTouchException( new Coordinate(aboveNeighborRow, startShipCoordinate.getColumn()).toLabel() + " ;Touch: " + cells[aboveNeighborRow][startShipCoordinate.getColumn()]);// Check the previous Col and the next col there was not ship in a vertical ship
            checkAboveRowAndBottomRowDotHaveTouchWithOurShip(cells, aboveNeighborRow, leftNeighborCol, bottomNeighborRow, rightNeighborCol);
        }

        return true;
    }

    private static void checkAboveRowAndBottomRowDotHaveTouchWithOurShip(Cell[][] cells, int prevRow, int prevCol, int nextRow, int nextCol) {
        for (int row = prevRow; row <= nextRow; row++) {
            if (cells[row][prevCol].isShip() || cells[row][nextCol].isShip())
                throw new ViolateTouchException( new Coordinate(row, prevCol).toLabel() + " ;Touched: " + cells[row][prevCol]);//DONT TOUCH
        }
    }

    private static void checkLeftColumnAndRightColumnDontTouchByNewShip(Cell[][] cells, int prevRow, int prevCol, int nextRow, int nextCol) {
        for (int col = prevCol; col <= nextCol; col++) {
            if (cells[prevRow][col].isShip() || cells[nextRow][col].isShip())
                throw new ViolateTouchException("row: " + prevRow + " ;col: " + col + " ;Touch: " + cells[prevRow][col]);
        }
    }

    /**
     * It checks if we have more than one type of ships in our fleet.
     */
    private void checkDuplicatedShipAdded(List<Ship> ships) {
        List<String> shipTypes = new ArrayList<>();
        for (Ship ship: ships) {
            String shipType = ship.getShipType().getName();
            if (shipTypes.contains(shipType)) {
                throw new DuplicateShipException("Duplicated ship found: "+shipType+" ;Coordinates: "+ship.getCoordinates().stream()
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
                throw new OutOfBoardException(ship.getShipType().name());
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
