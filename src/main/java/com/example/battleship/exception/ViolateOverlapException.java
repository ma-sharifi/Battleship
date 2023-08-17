package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class ViolateOverlapException extends BaseException{
    public ViolateOverlapException(String message) {
        super("Ships overlapped violated! "+message, ErrorCode.VIOLATE_OVERLAP_ERROR);
    }
}
