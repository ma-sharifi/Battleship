package com.example.battleship.exception.globalhandler;


import com.example.battleship.controller.dto.ResponseDto;
import com.example.battleship.exception.BaseException;
import com.example.battleship.exception.NotYourTurnException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Mahdi Sharifi
 */

@ControllerAdvice
@NoArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BaseException ex) {
        ResponseDto responseDto = new ResponseDto(ex.getErrorCode(),ex.getMessage());
        //Note: Error code is low level error code nad used for overriding message by third party
        log.error("#Error!"+" ; code is: "+responseDto.getErrorCode()+" ;Message is: "+responseDto.getMessage());
        return new ResponseEntity<>(responseDto, ex.getHttpStatus());
    }

}
