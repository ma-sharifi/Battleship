package com.example.battleship.controller;

import com.example.battleship.controller.dto.GameDto;
import com.example.battleship.controller.dto.GameFireResultDto;
import com.example.battleship.controller.dto.ShipDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Mahdi Sharifi
 */
public interface BattleshipGameController {
     @ApiResponses(value = {
             @ApiResponse(responseCode = "201", description = "Created a game. It will return game id that must be shared with players.",content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
             @ApiResponse(responseCode = "401", description = "If user and password don't provide by basic authentication", content = @Content),
             @ApiResponse(responseCode = "400", description = "Invalid Request. Any problem in client side will return this code.", content = @Content)})
     @Operation(summary = "Create a new game for players. Just one player can create a game, then player must give to the opponent.")
     @PostMapping
     ResponseEntity<GameDto>  createNewGame() throws URISyntaxException;

     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Join to already created game. After player join, they can place their fleet."),
             @ApiResponse(responseCode = "400", description = "Invalid Request. Any problem in client side will return this code.")})
     @Operation(summary = "Join to already created game.")
     @PostMapping("/{game-id}/join")
     ResponseEntity<Void> joinGame(
             @Parameter(description = "Game id refers to our game")
             @Valid @PathVariable("game-id") String gameId,
             @Parameter(description = "Which player is going to join. We have 2 player. player1 and player1. '1' stands for player1 ",example = "1")
             @RequestAttribute("player-id") int playerId
     );

     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Place their fleet on the board align with rules."),
             @ApiResponse(responseCode = "400", description = "Invalid Request. Any problem in client side will return this code.", content = @Content)})
     @Operation(summary = "Place their fleet on the board align with rules.")
     @PostMapping(value = "/{game-id}/place", consumes = MediaType.APPLICATION_JSON_VALUE)
     ResponseEntity<Void>  placeFleet(
             @Parameter(description = "Game id refers to our game",example = "138fe277-1474-4da7-897c-aad88c6dbcf4")
             @Valid @PathVariable("game-id") String gameId,
             @Parameter(description = "Which player is going to join. We have 2 player. player1 and player1. '1' stands for player1 ",example = "1")
             @RequestAttribute("player-id") int playerId,
             @Parameter(description = "List of ships that needs to be placed on board")
             @Valid  @RequestBody List<ShipDto> fleetDto);

     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Fire to opponent board."),
             @ApiResponse(responseCode = "400", description = "Invalid Request. Any problem in client side will return this code.", content = @Content)})
     @Operation(summary = "Fire to opponent board")
     @PostMapping(value ="/{game-id}/fire" , consumes = MediaType.TEXT_PLAIN_VALUE)
     ResponseEntity<GameFireResultDto>  fire(
             @Parameter(description = "Game id refers to our game",example = "138fe277-1474-4da7-897c-aad88c6dbcf4")
             @Valid @PathVariable("game-id") String gameId,
             @Parameter(description = "Which player is going to join. We have 2 player. player1 and player1. '1' stands for player1 ",example = "1")
             @RequestAttribute("player-id") int playerId,
             @Parameter(description = "Coordination of ship on board", example = "A1")
             @Valid @RequestBody String label);
}
