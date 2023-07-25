package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */
public class DuplicateFireException extends BaseException{
    public DuplicateFireException(String label) {
        super("You already fired this cell: "+label);
    }
}
