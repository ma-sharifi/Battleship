package com.example.battleship.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdi Sharifi
 */

@Getter
@Setter
public class GameFireResultDto extends ResponseDto {
    private String result;

    public GameFireResultDto(String result) {
        this.result = result;
    }

}
