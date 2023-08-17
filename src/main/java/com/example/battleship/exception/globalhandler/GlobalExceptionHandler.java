package com.example.battleship.exception.globalhandler;


import com.example.battleship.controller.dto.ErrorMessageDto;
import com.example.battleship.exception.BaseException;
import com.example.battleship.exception.OutOfBoardException;
import com.example.battleship.exception.errorcode.ErrorCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public static final String ERROR_CODE_MESSAGE = "#Error! Code is: %d ;Message is: %s";

    //If unhandled error occurs, this method will map it.
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequestException(Exception ex) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(ErrorCode.UNDANLDED_EXCEPTION_ERROR.code(),"Something unhandled occurred! message: "+ex.getMessage());
        log.error(String.format(ERROR_CODE_MESSAGE,errorMessageDto.errorCode(),errorMessageDto.message()));
        return ResponseEntity.status(ErrorCode.UNDANLDED_EXCEPTION_ERROR.httpStatus()).body(errorMessageDto);
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequestException(BaseException ex) {
        var errorMessageDto = ErrorMessageDto.of(ex);
        //Note: Error code is low-level error code nad used for an overriding message by third party
        log.error(String.format(ERROR_CODE_MESSAGE,errorMessageDto.errorCode(),errorMessageDto.message()));
        return ResponseEntity.status(ex.errorCode().httpStatus()).body(errorMessageDto);
    }

    /**
     * Effective Java: ITEM 72: FAVOR THE USE OF STANDARD EXCEPTIONS, removed ViolateGamePlayFlowException and is replaced
     * with IllegalStateException
     */
    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequestException(IllegalStateException ex) {
        var errorMessageDto = new ErrorMessageDto(ErrorCode.GAMEPLAY_FLOW_VIOLATED.code(),ex.getMessage());
        log.error(String.format(ERROR_CODE_MESSAGE ,errorMessageDto.errorCode(),errorMessageDto.message()));
        return new ResponseEntity<>(errorMessageDto, ErrorCode.GAMEPLAY_FLOW_VIOLATED.httpStatus());
    }

    @ExceptionHandler(value = OutOfBoardException.class)
    public ResponseEntity<ErrorMessageDto> handleBadRequestException(OutOfBoardException ex) {
        var errorMessageDto = new ErrorMessageDto(ex.errorCode().code(),ex.getMessage());
        log.error(String.format(ERROR_CODE_MESSAGE,errorMessageDto.errorCode(),errorMessageDto.message()));
        return ResponseEntity.status(ex.errorCode().httpStatus()).body(errorMessageDto);
    }

}
