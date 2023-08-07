package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class ViolateNumberOfShipException extends BaseException{
    public ViolateNumberOfShipException(String message) {
        super("Wrong number of ships is detected! expected:"+message,9, HttpStatus.BAD_REQUEST);
    }
}
