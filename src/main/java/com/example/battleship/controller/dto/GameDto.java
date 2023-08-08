package com.example.battleship.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Mahdi Sharifi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "When game successfully generated, this object will return to client as result")
public class GameDto extends ResponseDto {

    @JsonProperty("game_id")
    private String gameId;

    public GameDto(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "gameId='" + gameId + '\'' +
                '}' + super.toString();
    }
}
