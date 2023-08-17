package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class ViolateNumberOfShipException extends BaseException{
    public ViolateNumberOfShipException(String message) {
        super("Wrong number of ships is detected! expected:"+message, ErrorCode.VIOLATE_NUMBER_OF_SHIP_ERROR);
    }
}
