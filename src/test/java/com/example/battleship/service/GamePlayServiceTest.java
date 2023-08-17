package com.example.battleship.service;

import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.exception.*;
import com.example.battleship.model.Coordinate;
import com.example.battleship.model.ship.Direction;
import com.example.battleship.model.ship.Ship;
import com.example.battleship.model.ship.ShipType;
import com.example.battleship.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see GamePlayServiceImpl
 * <p>
 * Every senario is tested here
 */

@SpringBootTest
class GamePlayServiceTest {

    @Autowired
    GamePlayService gamePlayService;

    @Test
    void shouldReturnUUI_whenCreateNewGameIsCalled() {
        var battleshipGame = gamePlayService.createNewGame();
        assertNotNull(battleshipGame);
    }

    @Test
    void shouldJoinGameException_whenPlayerDidnotJoinGamed() {
        var gameId = gamePlayService.createNewGame();
        List<ShipDto> fleet1 = generateFleet1();

        var thrown = Assertions.assertThrows(PlayerDidNotJoinException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleet1);

        });
        assertTrue(thrown.getMessage().startsWith("Player did not joined yet! Please call joinGame first. Player"));
    }

    /**
     * All shot to ships must be hit if we palace it correctly on the board.
     */
    @Test
    void shouldPlaceShipOnBoardCorrectly_whenAddFleetIsCalled() {
        var gameId = gamePlayService.createNewGame();
        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);
        List<ShipDto> fleet1 = generateFleet1();
        List<ShipDto> fleet2 = generateFleet2();
        gamePlayService.placeFleet(gameId, 1, fleet1);
        gamePlayService.placeFleet(gameId, 2, fleet2);

        LinkedList<Ship> fleet = fleet2.stream().map(Util::shipDtoToShip).collect(Collectors.toCollection(LinkedList::new));

        int shipCounter = 0;
        for (Ship ship : fleet) {
            shipCounter++;
            for (int i = 0; i < ship.getCoordinates().size() - 1; i++) {
                Coordinate coordinate = ship.getCoordinates().get(i);
                assertEquals("Hit", gamePlayService.fire(gameId, 1, coordinate.toLabel()));
                gamePlayService.fire(gameId, 2, coordinate.toLabel());
            }
            if (shipCounter == fleet.size()) {
                assertTrue(gamePlayService.fire(gameId, 1, ship.getCoordinates().getLast().toLabel()).startsWith("Winner is: "));
            } else {
                assertEquals("Sunk", gamePlayService.fire(gameId, 1, ship.getCoordinates().getLast().toLabel()));
                gamePlayService.fire(gameId, 2, ship.getCoordinates().getLast().toLabel());
            }
        }

    }

    /**
     * If gameplay is violated, we should get error.
     */
    @Test
    void shouldThrowException_whenGanePlayViolated() {

        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        var thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
            gamePlayService.fire(gameId, 1, "A1");
        });

        assertTrue(thrown.getMessage().startsWith("Expected method call is"));
    }

    /**
     * If gameplay is violated, we should get error.
     */
    @Test
    void shouldThrowPlayerAlreadyJoinedException_whenPlayerWantedToJoinAgain() {

        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        var thrown = Assertions.assertThrows(PlayerAlreadyJoinedException.class, () -> {
            gamePlayService.joinGame(gameId, 1);
        });

        assertTrue(thrown.getMessage().startsWith("Player already joined! Player"));
    }

    @Test
    void shouldThrowNotYourTurnException_whenChangeTurnViolated() {
        var gameId = gamePlayService.createNewGame();
        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();
        List<ShipDto> fleet2 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);
        gamePlayService.placeFleet(gameId, 2, fleet2);

        NotYourTurnException thrown = Assertions.assertThrows(NotYourTurnException.class, () -> {
            gamePlayService.fire(gameId, 2, "A1");
        });
        assertTrue(thrown.getMessage().startsWith("It is not your turn! "));
    }

    @Test
    void shouldReturnSunk_whenSheepIsSunk() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 2, fleet2);

        assertTrue(gamePlayService.fire(gameId, 1, "A1").startsWith("Hit"));
        gamePlayService.fire(gameId, 2, "A1");
        gamePlayService.fire(gameId, 1, "A2");
        assertTrue(gamePlayService.fire(gameId, 2, "A2").startsWith("Sunk"));

    }


    @Test
    void shouldReturnWinner_whenContinueToPlayAfterAllShipSunk() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();
        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 1, fleet1);
        gamePlayService.placeFleet(gameId, 2, fleet2);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                gamePlayService.fire(gameId, 1, new Coordinate(i, j).toLabel());
                gamePlayService.fire(gameId, 2, new Coordinate(i, j).toLabel());
            }
        }
        gamePlayService.fire(gameId, 1, "I1");
        gamePlayService.fire(gameId, 2, "I1");
        gamePlayService.fire(gameId, 1, "I2");
        gamePlayService.fire(gameId, 2, "I2");
        assertTrue(gamePlayService.fire(gameId, 1, "I3").startsWith("Winner is: "));

    }

    /**
     * This is for testing if the game is finished, no one can fire.
     */
    @Test
    void shouldThrowGameAlreadyFinishedException_WhenOnePlayerWon() {//play the whole game
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        gamePlayService.placeFleet(gameId, 2, fleet2);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                gamePlayService.fire(gameId, 1, new Coordinate(i, j).toLabel());
                gamePlayService.fire(gameId, 2, new Coordinate(i, j).toLabel());
            }
        }
        gamePlayService.fire(gameId, 1, "I1");
        gamePlayService.fire(gameId, 2, "I1");
        gamePlayService.fire(gameId, 1, "I2");
        gamePlayService.fire(gameId, 2, "I2");
        assertTrue(gamePlayService.fire(gameId, 1, "I3").startsWith("Winner is: "));

        var thrown = Assertions.assertThrows(GameAlreadyFinishedException.class, () -> {
            gamePlayService.fire(gameId, 2, "I3");
        });
        assertTrue(thrown.getMessage().startsWith("Winner is: Player"));
    }

    @Test
    void shouldThrowDuplicateShipException_whenDuplicatedShipAddedToFleet() {
        var gameId = gamePlayService.createNewGame();
        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);
        List<ShipDto> fleet = new ArrayList<>();
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "D5"));
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "D9"));//Duplicate

        var thrown = Assertions.assertThrows(DuplicateShipException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleet);
        });
        assertTrue(thrown.getMessage().startsWith("Duplicated ship found: "));
    }

    @Test
    void shoulThrowViolateNumberOfShipException_whenCheckNumberOfShipInFleetIsNot5() {
        var gameId = gamePlayService.createNewGame();
        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);
        List<ShipDto> fleetDto = new ArrayList<>();
        fleetDto.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.VERTICAL, "I2"));
        fleetDto.add(new ShipDto(ShipType.DESTROYER, Direction.VERTICAL, "A1"));

        var thrown = Assertions.assertThrows(ViolateNumberOfShipException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleetDto);
        });
        assertTrue(thrown.getMessage().startsWith("Wrong number of ships is detected!"));
    }


    @Test
    void shouldThrowEGameNotCreatedYetException_whenJoinIsCalledWithoutNewGAmeTest() {
        var thrown = Assertions.assertThrows(GameNotCreatedYetException.class, () -> {
            gamePlayService.joinGame("gameId", 1);
        });
        assertTrue(thrown.getMessage().startsWith("Game not created yet or gameId is not correct!"));
    }

    @Test
    void shouldWrongCoordinateException_whenFirsIsCalleByWrongLabel() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 2, fleet2);

        WrongCoordinateException thrown = Assertions.assertThrows(WrongCoordinateException.class, () -> {
            gamePlayService.fire(gameId, 1, "PP");
        });

        assertTrue(thrown.getMessage().startsWith("Coordinate is wrong: "));
    }

    @Test
    void shouldThrowOutOfBoardException_whenShipIsOutOfABourdCoordination() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        LinkedList<ShipDto> fleet1 = generateFleet1();
        fleet1.removeLast();
        fleet1.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "D11"));

        var thrown = Assertions.assertThrows(OutOfBoardException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleet1);

        });
        assertTrue(thrown.getMessage().startsWith("Ship is out of board bound! Ship type: "));
    }

    @Test
    void shouldThrowOutOfBoardException_whenFirsIsCalledWithColumnOutOfABourdCoordination() {

        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 2, fleet2);

        var thrown = Assertions.assertThrows(OutOfBoardException.class, () -> {
            gamePlayService.fire(gameId, 1, "A11");

        });

        assertTrue(thrown.getMessage().startsWith("Fire coordinate is out of board bound! Label is:"));
    }

    @Test
    void shouldThrowOutOfBoardException_whenFirsIsCalledWithRowOutOfABourdCoordination() {

        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 2, fleet2);

        var thrown = Assertions.assertThrows(OutOfBoardException.class, () -> {
            gamePlayService.fire(gameId, 1, "O1");

        });

        assertTrue(thrown.getMessage().startsWith("Fire coordinate is out of board bound! Label is:"));
    }

    @Test
    void shouldThrowDuplicateShipException_whenOneShipUsed2Times() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();
        fleet1.add(fleet1.get(0));

        var thrown = Assertions.assertThrows(DuplicateShipException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleet1);

        });
        assertTrue(thrown.getMessage().startsWith("Duplicated ship found: "));

    }

    @Test
    void shouldThrowDuplicateFireException_whenFirsIsCalledWithOutOfABourdCoordination() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet1();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        List<ShipDto> fleet2 = generateFleet2();

        gamePlayService.placeFleet(gameId, 2, fleet2);

        gamePlayService.fire(gameId, 1, "A1");
        gamePlayService.fire(gameId, 2, "A2");

        var thrown = Assertions.assertThrows(DuplicateFireException.class, () -> {
            gamePlayService.fire(gameId, 1, "A1");

        });
        assertTrue(thrown.getMessage().startsWith("You already fired this cell:"));
    }


    @Test
    void shouldThrowVViolateOverlapException_when2shipTouchEachother() {
        var gameId = gamePlayService.createNewGame();
        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);
        LinkedList<ShipDto> fleet = generateFleet1();
        fleet.removeLast();
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "A2"));

        var thrown = Assertions.assertThrows(ViolateOverlapException.class, () -> {
            gamePlayService.placeFleet(gameId, 1, fleet);
        });
        assertTrue(thrown.getMessage().startsWith("Ships overlapped violated! "));

    }

    /**
     * 1 2 3 4 5 6 7 8 9 10
     * A A A A A A ~ ~ ~ ~ ~
     * B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * C ~ ~ B B B B ~ ~ ~ ~
     * D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * E ~ ~ ~ ~ S S S ~ ~ ~
     * F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * G ~ D D ~ ~ ~ ~ ~ ~ ~
     * H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * I C C C ~ ~ ~ ~ ~ ~ ~
     * J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     */
    private static List<ShipDto> generateFleet2() {
        List<ShipDto> fleet2 = new ArrayList<>();
        fleet2.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.HORIZONTAL, "A1"));
        fleet2.add(new ShipDto(ShipType.BATTLESHIP, Direction.HORIZONTAL, "C3"));
        fleet2.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "E5"));
        fleet2.add(new ShipDto(ShipType.DESTROYER, Direction.HORIZONTAL, "G2"));
        fleet2.add(new ShipDto(ShipType.CRUISER, Direction.HORIZONTAL, "I1"));
        return fleet2;
    }

    /**
     * 1 2 3 4 5 6 7 8 9 10
     * A D D ~ ~ ~ ~ ~ ~ ~ ~
     * B ~ ~ ~ ~ ~ ~ C C C ~
     * C ~ ~ B ~ ~ ~ ~ ~ ~ ~
     * D ~ ~ B ~ S ~ ~ ~ ~ ~
     * E ~ ~ B ~ S ~ ~ ~ ~ ~
     * F ~ ~ B ~ S ~ ~ ~ ~ ~
     * G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     * I ~ A A A A A ~ ~ ~ ~
     * J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
     */

    private static LinkedList<ShipDto> generateFleet1() {
        LinkedList<ShipDto> fleet1 = new LinkedList<>();
        fleet1.add(new ShipDto(ShipType.DESTROYER, Direction.HORIZONTAL, "A1"));
        fleet1.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.HORIZONTAL, "I2"));
        fleet1.add(new ShipDto(ShipType.CRUISER, Direction.HORIZONTAL, "B7"));
        fleet1.add(new ShipDto(ShipType.BATTLESHIP, Direction.VERTICAL, "C3"));
        fleet1.add(new ShipDto(ShipType.SUBMARINE, Direction.VERTICAL, "D5"));
        return fleet1;
    }

    private static LinkedList<ShipDto> generateFleet3() {
        LinkedList<ShipDto> fleet = new LinkedList<>();
        fleet.add(new ShipDto(ShipType.DESTROYER, Direction.HORIZONTAL, "A1"));    //A1A2
        fleet.add(new ShipDto(ShipType.CRUISER, Direction.HORIZONTAL, "B1"));
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "C1"));
        fleet.add(new ShipDto(ShipType.BATTLESHIP, Direction.HORIZONTAL, "D1"));
        fleet.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.HORIZONTAL, "E1"));
        return fleet;
    }

    private static LinkedList<ShipDto> generateFleet4() {
        LinkedList<ShipDto> fleet = new LinkedList<>();
        fleet.add(new ShipDto(ShipType.DESTROYER, Direction.VERTICAL, "A1"));
        fleet.add(new ShipDto(ShipType.CRUISER, Direction.VERTICAL, "A2"));
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.VERTICAL, "A3"));
        fleet.add(new ShipDto(ShipType.BATTLESHIP, Direction.VERTICAL, "A4"));
        fleet.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.VERTICAL, "A5"));
        return fleet;
    }

    @Test
    void shouldReturnWinner_whenContinueToPlayAfterAllShipSunkHorizontal() {
        var gameId = gamePlayService.createNewGame();

        gamePlayService.joinGame(gameId, 1);
        gamePlayService.joinGame(gameId, 2);

        List<ShipDto> fleet1 = generateFleet3();
        List<ShipDto> fleet2 = generateFleet4();

        gamePlayService.placeFleet(gameId, 1, fleet1);

        gamePlayService.placeFleet(gameId, 2, fleet2);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                gamePlayService.fire(gameId, 1, new Coordinate(i, j).toLabel());
                gamePlayService.fire(gameId, 2, new Coordinate(i, j).toLabel());
            }
        }
        gamePlayService.fire(gameId, 1, "E1");
        gamePlayService.fire(gameId, 2, "E1");
        gamePlayService.fire(gameId, 1, "E2");
        gamePlayService.fire(gameId, 2, "E2");
        gamePlayService.fire(gameId, 1, "E3");
        gamePlayService.fire(gameId, 2, "E3");
        gamePlayService.fire(gameId, 1, "E4");
        gamePlayService.fire(gameId, 2, "E4");

        assertTrue(gamePlayService.fire(gameId, 1, "E5").startsWith("Winner is: "));

    }
}