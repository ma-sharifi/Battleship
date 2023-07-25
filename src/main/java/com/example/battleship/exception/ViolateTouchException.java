package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */

public class ViolateTouchException extends BaseException{
    public ViolateTouchException(String message) {
        super("Ship is touched by another ship! "+message);
    }
}
