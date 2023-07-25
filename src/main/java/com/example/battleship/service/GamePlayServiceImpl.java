package com.example.battleship.service;


import com.example.battleship.Util;
import com.example.battleship.aspect.FlowControllerNeeded;
import com.example.battleship.aspect.GameIdNeeded;
import com.example.battleship.aspect.PlayerTurnControllerNeeded;
import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.exception.DuplicateFireException;
import com.example.battleship.exception.GameAlreadyFinishedException;
import com.example.battleship.exception.OutOfBoardException;
import com.example.battleship.model.BattleshipGame;
import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.Player;
import com.example.battleship.model.ship.Ship;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 *  @author Mahdi Sharifi
 *
 *  This is an implementation of BAttleship gmaeplay
 */
@Service
@Slf4j
public class GamePlayServiceImpl implements GamePlayService {

    //Help us to handle multiple game at the same time.
    //It means player1,2 can play gameId X and another player1,2 can play gameId Y
    private final Cache<String, BattleshipGame> battleshipGameCache;

    private final BoardService boardService;
    private final ValidatorService validatorService;

    public GamePlayServiceImpl(BoardService boardService, ValidatorService validatorService, Cache<String, BattleshipGame> battleshipGameCache) {
        this.boardService = boardService;
        this.validatorService = validatorService;
        this.battleshipGameCache = battleshipGameCache;
    }


    @Override
    public String createNewGame() { // Call it Once
        var game = new BattleshipGame();
        game.setId("CNG-" + UUID.randomUUID());//Mark the uuid the way we can realize it belongs to generated game.
        game.setTurn(1);
        battleshipGameCache.put(game.getId(), game);
        //We can notify the player2 about this UUID by sending notification(PushNotification,Kafka,Redis,..), SMS or telling him this UUID.
        log.info("#New game created: ", game.getId());
        return game.getId();
    }


    @Override
    @GameIdNeeded
    @PlayerTurnControllerNeeded
    public void joinGame(String gameId, int playerId) {
        var game = battleshipGameCache.getIfPresent(gameId);
        var player = new Player(playerId);
        int opponentId = (playerId % 2) + 1;
        //Player1/2 is already joined (Player2/1 joined after player1/2). The player1/2 is player2/1 opponent.
        game.setPlayerById(playerId, player);
        setPlayerOpponent(game, player, opponentId);
        log.info("#Player joined: Player" + playerId);
    }


    @Override
    @GameIdNeeded
    @FlowControllerNeeded
    @PlayerTurnControllerNeeded
    public void placeFleet(String gameId, int playerId, List<ShipDto> fleetDto) {
        var game = battleshipGameCache.getIfPresent(gameId);

        var player = game.getPlayer(playerId);
        var cells = player.getBoard();

        List<Ship> fleet = fleetDto.stream().map(Util::shipDtoToShip).toList();
        validatorService.validate(fleet);
        for (Ship ship : fleet) {
            if (validatorService.checkShipOverlap(cells, ship) && validatorService.checkShipDontTouchEachOther(cells, ship)) { //DOnt continue oif one hasproble
                placeShipOnBoard(cells, ship);
                player.placeShip(ship);
            }
        }
        log.info("#Fleet placed. Player" + playerId);
    }

    /**
     * Player1 fire on player2 field
     * Player2 fire on player1 field
     */
    @Override
    @FlowControllerNeeded
    @PlayerTurnControllerNeeded
    @GameIdNeeded
    public String fire(String gameId, int playerId, String label) { // A1
        var game = battleshipGameCache.getIfPresent(gameId);
        var player = game.getPlayer(playerId);
        if (game.isFinished()) throw new GameAlreadyFinishedException(String.valueOf(game.getWinner().getId()));

        Coordinate firedCoordinate = Coordinate.fromLabel(label);//Convert the label "A1" to coordinate (row=0,col=0)

        if (validatorService.coordinateIsOutOfBoard(firedCoordinate)) throw new OutOfBoardException(label);

        return placeShotOnBoardAndShipThenCalculateResult(game, player, firedCoordinate);
    }

    private static String placeShotOnBoardAndShipThenCalculateResult(BattleshipGame game, Player player, Coordinate firedCoordinate) {
        var result = "Miss";
        // Get the Opponent field since player1 fire on player2 field
        var firedCell = player.opponent().getBoard()[firedCoordinate.getRow()][firedCoordinate.getColumn()];
        var opponentShip = player.opponent().ship(firedCell.getShipId());

        if (firedCell.isShot())
            throw new DuplicateFireException(firedCoordinate.toLabel());//That cell already shot by player

        if (firedCell.isShip()) {
            firedCell.shot(); // Fast find Duplicate fire at the same position
            opponentShip.shot(firedCoordinate);
            if (opponentShip.isSunk()) {
                result = "Sunk";
                if (player.opponent().isFleetSunk()) {
                    game.setWinner(player);
                    result = "Winner is: Player" + player.getId();
                }
            } else result = "Hit";
        }
        return result;
    }

    private void placeShipOnBoard(Cell[][] cells, Ship ship) {
        ship.getCoordinates().forEach(coordinate -> cells[coordinate.getRow()][coordinate.getColumn()] = new Cell(ship.getShipType().getFirstCharacterOfType()));
    }

    /**
     * Player2 is already joined (Player1 joined after player2). The player2 is player1 opponent.
     */
    private void setPlayerOpponent(BattleshipGame game, Player player, int opponentId) {
        if (game.getPlayer(opponentId) != null) {
            player.setOpponent(game.getPlayer(opponentId));//Set player2 as opponent of player1
            game.getPlayer(opponentId).setOpponent(player);//Set player1 as opponent of player2
        }
    }

    public void printBoard(String gameId, int playerId) {
        var game = battleshipGameCache.getIfPresent(gameId);
        boardService.printBoard(game.getPlayer(playerId));
    }

}
