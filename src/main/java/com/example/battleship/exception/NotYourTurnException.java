package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class NotYourTurnException extends BaseException{
    public NotYourTurnException(String message) {
        super("It is not your turn! Now it is the turn of player"+message,6, HttpStatus.BAD_REQUEST);
    }
}
