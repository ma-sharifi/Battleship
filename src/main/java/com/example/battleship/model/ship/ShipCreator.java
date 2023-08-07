package com.example.battleship.model.ship;

/**
 * @author Mahdi Sharifi
 *
 * Create a new ship by ship type
 */
public interface ShipCreator {
    Ship newInstance();
}
