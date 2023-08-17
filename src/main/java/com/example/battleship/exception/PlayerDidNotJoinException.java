package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class PlayerDidNotJoinException extends BaseException {
    public PlayerDidNotJoinException(String playerId) {
        super("Player did not joined yet! Please call joinGame first. Player"+playerId, ErrorCode.PLAYER_DID_NOT_JOIN_ERROR);
    }
}
