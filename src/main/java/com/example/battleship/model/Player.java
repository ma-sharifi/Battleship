package com.example.battleship.model;

import com.example.battleship.model.ship.Ship;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mahdi Sharifi
 * The player is the owner of his fleet and his board.
 */
 @Setter @EqualsAndHashCode
public class Player {
    private int id; // 1 OR 2 . we have 2 player. player number 1 and player number 2
    private static final int BOARD_HEIGHT = 10;
    private static final int BOARD_WIDTH = 10;
    private Map<Character, Ship> fleet = new HashMap<>(); // The player has a fleet of ships.
    private String nextAction; //Used for control the flow of gameplay. It means placeFleet can't call before joinGame
    private Player opponent; //who is player's opponent
    //Players keep track of their guesses and the hits or misses.
    private Cell[][] board = new Cell[BOARD_HEIGHT][BOARD_WIDTH]; // Player's field. Without players, the field does not have a concept.

    public Cell[][] getBoard() {
        return board;
    }

    public int getId() {
        return id;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void placeShip(Ship ship) {
        fleet.put(ship.type().id(), ship);
    }

    public Player opponent() {
        return opponent;
    }

    public Ship ship(Character ship) {
        return fleet.get(ship);
    }

    public boolean isFleetSunk() {
        return fleet.values().stream().allMatch(Ship::isSunk);
    }

    public Player(int id) {
        this.nextAction = "placeFleet";//Used for control the flow of gameplay. After the creation of player next action is "placeFleet"
        this.id = id;
        initilaizeBoard();
    }

    private void initilaizeBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
