package com.example.battleship.model.ship;


import com.example.battleship.model.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @author Mahdi Sharifi
 * It has the parent of all ships. The ship holds the information about the ship.
 */
@Setter @Getter
public abstract class Ship {
    private final Character id;// The character C stands for cruise, and the character B stands for battleship.
    private Direction direction;
    private final ShipType type;
    private LinkedList<Coordinate> coordinates = new LinkedList<>(); //I used Linkedlist in order to get last and first item easily

     Ship(ShipType type) {
        this.type = type;
        this.id = type.id();
    }

    public void shot(Coordinate coordinate) {
        for (Coordinate shipLocation : coordinates) {
            if (shipLocation.isSameLocation(coordinate)) shipLocation.setHit(true);
        }
    }

    public void setId(Character id) {//Do nothing, it provide automatically by irst character of shipType
    }

    public boolean isShot(Coordinate coordinate) { //The result of shot is hit
        return coordinates.stream().filter(coordinate::isSameLocation).anyMatch(Coordinate::isHit);
    }

    public boolean isShot(int row, int col) {
        return isShot(new Coordinate(row, col));
    }

    public boolean isSunk() {
        return coordinates.stream().allMatch(Coordinate::isHit);
    }

    public ShipType type(){
        return type;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
//                ", direction=" + direction +
//                ", shipType=" + type +
//                ", coordinates=" + coordinates +
                '}';
    }
}
