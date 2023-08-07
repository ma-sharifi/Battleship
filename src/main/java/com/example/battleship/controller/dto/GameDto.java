package com.example.battleship.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Mahdi Sharifi
 */
@Data
public class GameDto extends ResponseDto {

    @JsonProperty("game_id")
    private String id;

    public GameDto(String gameId) {
        this.id = gameId;
    }
}
