package com.example.battleship.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mahdi Sharifi
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "When game successfully generated, this object will return to client as result")
public record GameDto(@JsonProperty("game_id") String gameId) {}