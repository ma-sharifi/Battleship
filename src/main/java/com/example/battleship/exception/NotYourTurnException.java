package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */

public class NotYourTurnException extends BaseException {
    public NotYourTurnException(String message) {
        super("It is not your turn! Now it is the turn of player" + message, ErrorCode.NOT_YOUR_TURN_ERROR);
    }
}
