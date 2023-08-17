package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */
public class DuplicateShipException extends BaseException {
    public DuplicateShipException(String message) {
        super("Duplicated ship found: " + message, ErrorCode.DUPLICATE_SHIP_ERROR);
    }
}
