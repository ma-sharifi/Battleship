package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class PlayerAlreadyJoinedException extends BaseException {
    public PlayerAlreadyJoinedException(String playerId) {
        super("Player already joined! Player"+playerId, ErrorCode.PLAYER_ALREADY_JOINED_ERROR);
    }
}
