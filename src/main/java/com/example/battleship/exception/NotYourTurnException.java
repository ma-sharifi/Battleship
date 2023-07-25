package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class NotYourTurnException extends BaseException{
    public NotYourTurnException(String message) {
        super("It is not your turn! Now it is the turn of player"+message);
    }
}
