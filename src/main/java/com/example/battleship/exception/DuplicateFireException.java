package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class DuplicateFireException extends BaseException{
    public DuplicateFireException(String label) {
        super("You already fired this cell: "+label, 1, HttpStatus.BAD_REQUEST);
    }
}
