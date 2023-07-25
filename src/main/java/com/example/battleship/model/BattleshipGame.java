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

    public Player getPlayer(int playerId) {
        if (playerId<=players.size() && playerId >0)
            return players.get(playerId-1);
        return null;
    }

    public void setPlayerById(int playerId,Player player) {
        if(getPlayer(playerId)==null)
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

    public void changeTurn() {
        turn = ((turn % 2) + 1);
    }

    public boolean isPlayerTurn(int playerId) {
        return getTurn() == playerId;
    }

}
