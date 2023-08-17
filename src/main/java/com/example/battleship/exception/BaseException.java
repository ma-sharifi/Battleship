package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Mahdi Sharifi
 */
@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private final ErrorCode errorCode;
    public BaseException(String message,ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
