package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */

public class WrongCoordinateException extends BaseException {

    public WrongCoordinateException(String coordinate) {
        super(String.format("Coordinate is wrong: %s", coordinate));
    }

}
