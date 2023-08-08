package com.example.battleship.controller;

import com.example.battleship.BattleshipApplication;
import com.example.battleship.controller.dto.GameDto;
import com.example.battleship.controller.dto.ShipDto;
import com.example.battleship.model.ship.Direction;
import com.example.battleship.model.ship.ShipType;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mahdi Sharifi
 * @see BattleshipGameController
 */

@SpringBootTest(classes = BattleshipApplication.class)
@AutoConfigureMockMvc
class BattleshipGameControllerImplTest {

    private static final String API_URL = "/v1/battleship";
    private static final String BASIC_AUTH_PALYER1 = "Basic cGxheWVyMTpwYXNzd29yZA==";
    private static final String BASIC_AUTH_PALYER2 = "Basic cGxheWVyMjpwYXNzd29yZA==";
    private static final String BASIC_AUTH_WRONG_PALYER = "Basic cGxheWVyMzpwYXNzd29yZA==";

    private final static Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateNewGame_whenCreateNewGameIsCalled() throws Exception {
        mockMvc
                .perform(post(API_URL).header("Authorization", BASIC_AUTH_PALYER1))
                .andExpect(status().isCreated());
    }
 @Test
    void shouldReturnError_whenCreateNewGameIsCalledWithoutBasicAuthentication() throws Exception {
        mockMvc
                .perform(post(API_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(22));
    }

    @Test
    void shouldReturnError_whenCreateNewGameIsCalledWithWrongUserPass() throws Exception {
        mockMvc
                .perform(post(API_URL).header("Authorization", BASIC_AUTH_WRONG_PALYER))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(22));
    }

    @Test
    void shouldPlayerJoinGame_whenJoinGameIsCalled() throws Exception {
        var gameId = createNewGame();

        joinGame(gameId);
    }


    @Test
    void shouldPlaceFleet_whenPlaceFleetIsCalled() throws Exception {

        var gameId = createNewGame();
        joinGame(gameId);

        List<ShipDto> fleet1 = generateFleet1();

        placeShip(gameId, fleet1);
    }

    @Test
    void shouldShootShips_whenFireIsCalled() throws Exception {
        var gameId = createNewGame();

        joinGame(gameId);

        List<ShipDto> fleet1 = generateFleet1();

        placeShip(gameId, fleet1);

        mockMvc
                .perform(post(API_URL + "/{game-id}/fire", gameId)
                        .header("Authorization", BASIC_AUTH_PALYER1)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("A1")
                )
                .andExpect(status().isOk());
    }

    private void placeShip(String gameId, List<ShipDto> fleet1) throws Exception {
        mockMvc
                .perform(post(API_URL + "/{game-id}/place", gameId)
                        .header("Authorization", BASIC_AUTH_PALYER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(fleet1))
                )
                .andExpect(status().isOk());

        mockMvc
                .perform(post(API_URL + "/{game-id}/place", gameId)
                        .header("Authorization", BASIC_AUTH_PALYER2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(fleet1))
                )
                .andExpect(status().isOk());
    }

    private void joinGame(String gameId) throws Exception {
        mockMvc
                .perform(post(API_URL + "/{game-id}/join", gameId)
                        .header("Authorization", BASIC_AUTH_PALYER1))
                .andExpect(status().isOk());

        mockMvc
                .perform(post(API_URL + "/{game-id}/join", gameId)
                        .header("Authorization", BASIC_AUTH_PALYER2))
                .andExpect(status().isOk());
    }

    private static LinkedList<ShipDto> generateFleet1() {
        LinkedList<ShipDto> fleet = new LinkedList<>();
        fleet.add(new ShipDto(ShipType.DESTROYER, Direction.HORIZONTAL, "A1"));
        fleet.add(new ShipDto(ShipType.CRUISER, Direction.HORIZONTAL, "B1"));
        fleet.add(new ShipDto(ShipType.SUBMARINE, Direction.HORIZONTAL, "C1"));
        fleet.add(new ShipDto(ShipType.BATTLESHIP, Direction.HORIZONTAL, "D1"));
        fleet.add(new ShipDto(ShipType.AIRCRAFT_CARRIER, Direction.HORIZONTAL, "I1"));
        return fleet;
    }

    private String createNewGame() throws Exception {
        MvcResult result = mockMvc
                .perform(post(API_URL)
                        .header("Authorization", BASIC_AUTH_PALYER1))
                .andExpect(status().isCreated()).andReturn();
        GameDto responseDto = GSON.fromJson(result.getResponse().getContentAsString(),GameDto.class);
        return responseDto.getGameId();
    }

}