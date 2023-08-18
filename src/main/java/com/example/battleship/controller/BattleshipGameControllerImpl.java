package com.example.battleship.controller;

import com.example.battleship.controller.dto.GameDto;
import com.example.battleship.controller.dto.GameFireResultDto;
import com.example.battleship.controller.dto.LabelDto;
import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.service.GamePlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Mahdi Sharifi
 * <p>
 * It exposes the API of the battleship game.
 */

@RestController
@Slf4j
@RequestMapping(value = "/v1/battleship")
public class BattleshipGameControllerImpl implements BattleshipGameController {

    private final GamePlayService gamePlayService;

    public BattleshipGameControllerImpl(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public ResponseEntity<GameDto> createNewGame() throws URISyntaxException {
        var gameId = gamePlayService.createNewGame();
        return ResponseEntity
                .created(new URI("/v1/battleship/" + gameId))
                .body(new GameDto(gameId));
    }

    @Override
    public ResponseEntity<Void> joinGame(String gameId, int playerId) {
        gamePlayService.joinGame(gameId, playerId);
        return ResponseEntity.ok().build();  //I used 200 not 201, because no resource was created that I can address by URI.
    }

    @Override
    public ResponseEntity<Void> placeFleet(String gameId, int playerId, List<ShipDto> fleetDto) {
        gamePlayService.placeFleet(gameId, playerId, fleetDto);
        return ResponseEntity.ok().build();//I used 200 not 201, because no resource was created that I can address by URI.
    }

    @Override
    public ResponseEntity<GameFireResultDto> fire(String gameId, int playerId, LabelDto labelDto) {
        return ResponseEntity
                .ok(new GameFireResultDto(gamePlayService.fire(gameId, playerId, labelDto.label())));
    }

}
