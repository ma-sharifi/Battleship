package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class GameAlreadyFinishedException extends BaseException{
    public GameAlreadyFinishedException(String playerId) {
        super("Winner is: Player" + playerId, 3, HttpStatus.BAD_REQUEST);
    }
}
