package com.example.battleship.controller.dto;

import com.example.battleship.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mahdi Sharifi
 */

@Schema(description = " ErrorMessage will return when we faced an exception")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorMessageDto(@JsonProperty("error_code") int errorCode, String message) {
    public static ErrorMessageDto of(BaseException baseException){
        return new ErrorMessageDto(baseException.getErrorCode().code(), baseException.getMessage());
    }
}
