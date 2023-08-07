package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class ShipDoesNotExistException extends BaseException{
    public ShipDoesNotExistException(String message) {
        super("Ship does not exist with this id: "+message,20,HttpStatus.BAD_REQUEST);//TODO change the way we set error code
    }
}
