package com.example.battleship.model;


import lombok.Data;
/**
 * @author Mahdi Sharifi
 *
 * Our board is composed of matrix of cells.
 */
@Data
public class Cell {

    private Character shipId; // store the id of ship on board
    private boolean shot; //This indicates that the fire did not hit the ship, it hits the water. I take it as Miss.

    public Cell() {
    }

    public Cell(Character shipId) {
        this.shipId = shipId;
    }

    public boolean isWater() {
        return shipId == null;
    }

    public boolean isShot() {
        return shot;
    }

    public void shot() {
        this.shot = true;
    }

    public boolean isShip() {
        return shipId != null;
    }


}
