package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */

public class ViolateTouchException extends BaseException{
    public ViolateTouchException(String message) {
        super("Ship is touched by another ship! "+message,11, HttpStatus.BAD_REQUEST);
    }
}
