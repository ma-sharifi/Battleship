package com.example.battleship.exception;

import com.example.battleship.exception.errorcode.ErrorCode;

/**
 * @author Mahdi Sharifi
 */

public class DuplicateFireException extends BaseException{
    public DuplicateFireException(String label) {
        super("You already fired this cell: "+label, ErrorCode.DUPLICATE_FIRE_ERROR);
    }
}
