package com.example.battleship.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mahdi Sharifi
 */

@Data
@Schema(description = "Response is always a JSON for sake of uniform implementation in client side")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    @JsonProperty("error_code")
    private int errorCode;
    @JsonIgnore
    private HttpStatus httpStatus=HttpStatus.OK;
    private String message = "Success";
    private Object payload ;

    public ResponseDto(Object payload) {
         this.payload=payload;
    }
}
