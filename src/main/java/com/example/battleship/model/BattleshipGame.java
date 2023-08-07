package com.example.battleship.model;

import com.example.battleship.exception.JoinGameException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahdi Sharifi
 */
@Data
public class BattleshipGame {
    private String id;
    private List<Player> players=new ArrayList<>(); //we can easily switch between players without witch case.
    private int turn;
    private Player winner;

    public Player playerById(int playerId) {
        if (playerId<=players.size() && playerId >0)
            return players.get(playerId-1);
        return null;
    }

    public void setPlayerById(int playerId,Player player) {
        if(playerById(playerId)==null)
            players.add(playerId-1,player);
        else
            throw new JoinGameException(String.valueOf(playerId));
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isFinished(){
        return winner!=null;
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

    public boolean isPlayerTurn(int playerId) {
        return getTurn() == playerId;
    }
}
