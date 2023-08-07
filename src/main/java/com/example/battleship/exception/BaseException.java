package com.example.battleship.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */
@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private final int errorCode;
    private final HttpStatus httpStatus; // NOT_FOUND , OK
    public BaseException(String message, int errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
