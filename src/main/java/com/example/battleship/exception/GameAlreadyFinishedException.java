package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class GameAlreadyFinishedException extends BaseException{
    public GameAlreadyFinishedException(String playerId) {
        super("Winner is: Player" + playerId);
    }
}
