package com.example.battleship.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * @author Mahdi Sharifi
 */
public class JoinGameException extends BaseException {
    public JoinGameException(String playerId) {
        super("Player already joined the game! Player"+playerId);
    }
}
