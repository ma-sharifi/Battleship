package com.example.battleship.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Mahdi Sharifi
 */

@Data
@Schema(description = "Response is always a JSON for sake of uniform implementation in client side")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    @JsonProperty("error_code")
    private int errorCode;
    private String message;

    public ResponseDto(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseDto() {
        this.errorCode = 0;
        this.message = "Success";
    }
}
