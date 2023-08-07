package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class GameNotCreatedYetException extends BaseException {
    public GameNotCreatedYetException(String gameId) {
        super("Game not created yet or gameId is not correct! GameId: "+gameId,4, HttpStatus.BAD_REQUEST);
    }
}
