package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class GameNotCreatedYetException extends BaseException {
    public GameNotCreatedYetException(String gameId) {
        super("Game not created yet or gameId is not correct! GameId: "+gameId, ErrorCode.GAME_NOT_CREATED_YET_ERROR);
    }
}
