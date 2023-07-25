package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class ViolateOverlapException extends BaseException{
    public ViolateOverlapException(String message) {
        super("Ship has overlap by another ship! "+message);
    }
}
