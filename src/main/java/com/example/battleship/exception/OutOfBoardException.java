package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class OutOfBoardException extends IndexOutOfBoundsException{
    private final ErrorCode errorCode;
    public OutOfBoardException(String message,ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
