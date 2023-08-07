package com.example.battleship.exception;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class JoinGameException extends BaseException {
    public JoinGameException(String playerId) {
        super("Player already joined the game! Player"+playerId,5, HttpStatus.BAD_REQUEST);
    }
}
