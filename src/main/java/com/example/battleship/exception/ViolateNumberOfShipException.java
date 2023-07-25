package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class ViolateNumberOfShipException extends BaseException{
    public ViolateNumberOfShipException(String message) {
        super("Wrong number of ships is detected! expected:"+message);
    }
}
