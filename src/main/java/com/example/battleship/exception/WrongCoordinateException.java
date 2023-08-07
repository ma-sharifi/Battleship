package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */

public class WrongCoordinateException extends BaseException {

    public WrongCoordinateException(String coordinate) {
        super(String.format("Coordinate is wrong: %s", coordinate),12, HttpStatus.BAD_REQUEST);
    }

}
