package com.example.battleship.exception;


import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class GameAlreadyFinishedException extends BaseException{
    public GameAlreadyFinishedException(String playerId) {
        super("Winner is: Player" + playerId, ErrorCode.GAME_ALREADY_FINISHED_ERROR);
    }
}
