package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class OutOfBoardException extends BaseException{
    public OutOfBoardException(String shipType) {
        super("Ship is out of board bound! Ship type: "+shipType);
    }
}
