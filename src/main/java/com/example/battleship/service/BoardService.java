package com.example.battleship.service;

import com.example.battleship.model.Cell;
import com.example.battleship.model.Player;
import org.springframework.stereotype.Service;

/**
 * @author Mahdi Sharifi
 */
@Service
public class BoardService {
    private static final char WATER = '~';

    public void printBoard(Player player) {
        Cell[][] board = player.getBoard();
        System.out.print("  ");
        for (int i = 0; i < board.length; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < board[0].length; j++) {

                if (board[i][j].isShip()) {
                    if (player.ship(board[i][j].getShipId()).isShot(i, j)) System.out.print("H ");
                    else if (board[i][j].isShot()) System.out.print(". ");
                    else System.out.print(board[i][j].getShipId() + " ");
                }
                if(!board[i][j].isShot() && board[i][j].isWater()) System.out.print(WATER + " ");

            }
            System.out.println();
        }
    }
}
