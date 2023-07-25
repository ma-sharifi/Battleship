package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class GameNotCreatedYetException extends BaseException {
    public GameNotCreatedYetException(String gameId) {
        super("Game not created yet or gameId is not correct! GameId: "+gameId);
    }
}