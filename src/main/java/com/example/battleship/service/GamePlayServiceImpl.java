package com.example.battleship.service;


import com.example.battleship.aspect.FlowControllerNeeded;
import com.example.battleship.aspect.GameIdNeeded;
import com.example.battleship.aspect.PlayerTurnControllerNeeded;
import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.exception.DuplicateFireException;
import com.example.battleship.exception.GameAlreadyFinishedException;
import com.example.battleship.exception.OutOfBoardException;
import com.example.battleship.exception.PlayerDidNotJoinException;
import com.example.battleship.exception.errorcode.ErrorCode;
import com.example.battleship.model.BattleshipGame;
import com.example.battleship.model.Cell;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.Player;
import com.example.battleship.model.ship.Ship;
import com.example.battleship.util.Util;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Mahdi Sharifi
 * <p>
 * This is an implementation of Battleship Gameplay
 */
@Service
@Slf4j
public class GamePlayServiceImpl implements GamePlayService {

    public static final String GAME_ID_S_PLAYER_JOINED_PLAYER_D = "#Game id: %s ;Player joined. Player%d";
    public static final String GAME_ID_S_FLEET_PLACED_PLAYER_D = "#Game id: %s ;Fleet placed. Player%d";
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
        log.info("#New game created. Game id is: " + game.getId());
        return game.getId();
    }


    @Override
    @GameIdNeeded
    @PlayerTurnControllerNeeded
    public void joinGame(String gameId, int playerId) {
        var game = battleshipGameCache.getIfPresent(gameId);
        //Player1/2 is already joined (Player2/1 joined after player1/2). The player1/2 is player2/1 opponent.
        Objects.requireNonNull(game).setPlayers(playerId);
        log.info(String.format(GAME_ID_S_PLAYER_JOINED_PLAYER_D, gameId, playerId));
    }

    @Override
    @GameIdNeeded
    @FlowControllerNeeded
    @PlayerTurnControllerNeeded
    public void placeFleet(String gameId, int playerId, List<ShipDto> fleetDto) {
        var game = battleshipGameCache.getIfPresent(gameId);

        var player = Objects.requireNonNull(game).playerById(playerId)
                .orElseThrow(() -> new PlayerDidNotJoinException(String.valueOf(playerId)));
        var field = player.getBoard();

        List<Ship> fleet = fleetDto.stream().map(Util::shipDtoToShip).toList();
        validatorService.validate(fleet);
        for (Ship ship : fleet) {
            placeShipOnBoard(field, ship);
            player.placeShip(ship);
        }
        log.info(String.format(GAME_ID_S_FLEET_PLACED_PLAYER_D, gameId, playerId));
    }

    /**
     * Player1 fire on player2 field
     * Player2 fire on player1 field
     */
    @Override
    @FlowControllerNeeded
    @PlayerTurnControllerNeeded
    @GameIdNeeded
    public String fire(String gameId, int playerId, String label) {
        var game = battleshipGameCache.getIfPresent(gameId);
        var player = Objects.requireNonNull(game).playerById(playerId).orElseThrow(() -> new PlayerDidNotJoinException(String.valueOf(playerId)));
        if (game.isFinished()) throw new GameAlreadyFinishedException(String.valueOf(game.getWinner().getId()));

        Coordinate firedCoordinate = Coordinate.fromLabel(label);//Convert the label "A1" to coordinate (row=0,col=0)

        if (validatorService.coordinateIsOutOfBoard(firedCoordinate))
            throw new OutOfBoardException("Fire coordinate is out of board bound! Label is: " + label + " Board range is row(A-G) and col(1-10) ", ErrorCode.FIRE_IS_OUT_OF_BOARD_ERROR);

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
        log.info("#Game id: " + game.getId() + " Player " + player + " fire at " + firedCoordinate.toLabel() + " ;result is: " + result);
        return result;
    }

    private void placeShipOnBoard(Cell[][] cells, Ship ship) {
        ship.getCoordinates().forEach(coordinate -> cells[coordinate.getRow()][coordinate.getColumn()] = new Cell(ship.type().id()));
    }

    //    Used for visualizing view of the fleet on board
    public void printBoard(String gameId, int playerId) {
        var game = battleshipGameCache.getIfPresent(gameId);
        var playerOptional = Objects.requireNonNull(game).playerById(playerId);
        if (playerOptional.isPresent()) {
            boardService.printBoard(playerOptional.get());
        } else log.error("#Player id is not present! player: " + playerId);
    }

}
