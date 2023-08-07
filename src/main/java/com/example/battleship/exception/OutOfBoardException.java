package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class OutOfBoardException extends BaseException{
    public OutOfBoardException(String shipType) {
        super("Ship is out of board bound! Ship type: "+shipType,7,HttpStatus.BAD_REQUEST);
    }
}
