package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class PlayerDidNotJoinException extends BaseException {
    public PlayerDidNotJoinException(String playerId) {
        super("Player did not joined yet! Please call joinGame first. Player"+playerId,8, HttpStatus.BAD_REQUEST);
    }
}
