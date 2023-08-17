package com.example.battleship.model;

import com.example.battleship.exception.PlayerAlreadyJoinedException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mahdi Sharifi
 */
@Data
public class BattleshipGame {
    private String id;
    private List<Player> players = new ArrayList<>(); //we can easily switch between players without switch case.
    private int turn;
    private Player winner;

    public Optional<Player> playerById(int playerId) {
        if (playerId <= players.size())
            return Optional.of(players.get(playerId - 1));
        return Optional.empty();
    }

    public void setPlayers(int playerId) {
        if (playerById(playerId).isEmpty()) {
            var player = new Player(playerId);
            players.add(playerId - 1, player);
            setPlayerOpponent(player);
        } else
            throw new PlayerAlreadyJoinedException(String.valueOf(playerId));
    }

    /**
     * Player2 is already joined (Player1 joined after player2). The player2 is player1 opponent.
     */
    private void setPlayerOpponent(Player player) {
        int opponentId= (player.getId() % 2) + 1; //find opponent id and set it as player's opponent
        Optional<Player> opponentOptional = playerById(opponentId);
        if (opponentOptional.isPresent()) {
            Player opponent = opponentOptional.get();
            player.setOpponent(opponent);//Set player2 as opponent of player1
            opponent.setOpponent(player);//Set player1 as opponent of player2
        }
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isFinished() {
        return winner != null;
    }

    /**
     * Effective Java: Chapter 11, Item 78
     * It is not sufficient to synchronize only the write method!
     * Synchronization is not guaranteed to work unless both read and write operations are synchronized.
     */
    public synchronized int getTurn() {
        return turn;
    }

    public synchronized void setTurn(int turn) {
        this.turn = turn;
    }

    public synchronized void changeTurn() {
        turn = ((turn % 2) + 1);
    }

}
