package com.example.battleship.service;

import com.example.battleship.controller.dto.ShipDto;

import java.util.List;

/**
 * @author Mahdi Sharifi
 *
 * This interface is a contract for Battleship gameplay
 */

public interface GamePlayService {
    /**
     * After player create the game, it must share the gameId with another player.
     */
    String createNewGame();

    /**
     * Users must join the game then they can play. The only 2 players (player1 and player2) can join the game.
     * This method creates players and assigns their opponent;
     */

    void joinGame(String gameId, int playerId);

    /**
     * It placed ships on the board based on 3 fields of shipDto.
     * ShipDto has 3 fields, there is no need to set all coordinations of the ship since we can calculate them with
     * the help of Direction and ShipType
     */
    void placeFleet(String gameId, int playerId, List<ShipDto> fleetDto);

    /**
     *The player guesses the location of the opponent's ship, then shoots it by presenting a label like A1.
     * If the player guessed the correct ship coordinate, it will get Hit otherwise it will get Miss.
     */
    String fire(String gameId, int playerId, String label);

}
