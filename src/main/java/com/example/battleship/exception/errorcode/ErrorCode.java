package com.example.battleship.exception.errorcode;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */

public enum ErrorCode {
    UNDANLDED_EXCEPTION_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_FIRE_ERROR(2, HttpStatus.BAD_REQUEST),
    DUPLICATE_SHIP_ERROR(3,HttpStatus.BAD_REQUEST),
    GAME_ALREADY_FINISHED_ERROR(4,HttpStatus.BAD_REQUEST),
    GAME_NOT_CREATED_YET_ERROR(5,HttpStatus.BAD_REQUEST),
    JOIN_GAME_ERROR(6,HttpStatus.BAD_REQUEST),
    NOT_YOUR_TURN_ERROR(7,HttpStatus.BAD_REQUEST),
    OUT_OF_BOARD_ERROR(8,HttpStatus.BAD_REQUEST),
    FIRE_IS_OUT_OF_BOARD_ERROR(9,HttpStatus.BAD_REQUEST),
    PLAYER_DID_NOT_JOIN_ERROR(10,HttpStatus.BAD_REQUEST),
    PLAYER_ALREADY_JOINED_ERROR(11,HttpStatus.BAD_REQUEST),
    VIOLATE_NUMBER_OF_SHIP_ERROR(12,HttpStatus.BAD_REQUEST),
    VIOLATE_OVERLAP_ERROR(13,HttpStatus.BAD_REQUEST),
    WRONG_COORDINATE_ERROR(14,HttpStatus.BAD_REQUEST),
    USERNAME_OR_PASSWORD_NOT_MATCH(15,HttpStatus.UNAUTHORIZED),
    GAMEPLAY_FLOW_VIOLATED(16,HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatus httpStatus;

    public int code() {
        return code;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    ErrorCode(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
