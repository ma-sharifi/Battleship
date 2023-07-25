package com.example.battleship.exception;

/**
 * @author Mahdi Sharifi
 */

public class ViolateGamePlayFlowException extends BaseException {

    public ViolateGamePlayFlowException(String expectedMethod,String realMethodName) {
        super(String.format("Expected method call is: %s ,and real method called is: %s", expectedMethod,realMethodName));
    }

}
