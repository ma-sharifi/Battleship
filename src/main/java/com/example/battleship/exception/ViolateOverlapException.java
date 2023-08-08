package com.example.battleship.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
public class ViolateOverlapException extends BaseException{
    public ViolateOverlapException(String message) {
        super("Ships overlapped violated! "+message,10, HttpStatus.BAD_REQUEST);
    }
}
