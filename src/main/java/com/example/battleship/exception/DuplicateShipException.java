package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class DuplicateShipException extends BaseException{
    public DuplicateShipException(String message) {
        super("Duplicated ship found: "+message,2,HttpStatus.BAD_REQUEST);
    }
}
