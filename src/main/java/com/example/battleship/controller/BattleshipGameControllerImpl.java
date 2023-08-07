package com.example.battleship.controller;

import com.example.battleship.controller.dto.GameDto;
import com.example.battleship.controller.dto.GameFireResultDto;
import com.example.battleship.controller.dto.ResponseDto;
import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.service.GamePlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Mahdi Sharifi
 *
 * It expose the API of game.
 */

@RestController
@Slf4j
@RequestMapping(value="/v1/battleship")
public class BattleshipGameControllerImpl implements BattleshipGameController{

    private final GamePlayService gamePlayService;

    public BattleshipGameControllerImpl(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public ResponseEntity<ResponseDto> createNewGame() throws URISyntaxException {
        var gameId=gamePlayService.createNewGame();
        return ResponseEntity
                .created(new URI( "/v1/battleship/"+gameId))
                .body(new GameDto(gameId));
    }

    @Override
    public  ResponseEntity<ResponseDto> joinGame(String gameId,int playerId) {
        gamePlayService.joinGame(gameId,playerId);
        return ResponseEntity
                .ok(new ResponseDto());  //I used 200 not 201, because no resource was created that I can address by UIR.
    }

    @Override
    public ResponseEntity<ResponseDto> placeFleet(String gameId,int playerId,List<ShipDto> fleetDto)  {
        gamePlayService.placeFleet(gameId,playerId,fleetDto);
        return ResponseEntity
                .ok(new ResponseDto());//I used 200 not 201, because no resource was created that I can address by UIR.
    }

    @Override
    public ResponseEntity<ResponseDto>  fire(String gameId, int playerId,String label) {
        return ResponseEntity
                .ok(new GameFireResultDto(gamePlayService.fire(gameId,playerId,label)));
    }

}
