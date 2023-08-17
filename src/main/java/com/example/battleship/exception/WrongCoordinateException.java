package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */

public class WrongCoordinateException extends BaseException {

    public WrongCoordinateException(String coordinate) {
        super("Coordinate is wrong: "+ coordinate, ErrorCode.WRONG_COORDINATE_ERROR);
    }

}
