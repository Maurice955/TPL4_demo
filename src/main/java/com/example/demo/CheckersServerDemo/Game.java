package com.example.demo.CheckersServerDemo;

import java.util.Arrays;

public abstract class Game {

    //reference to the Player which has to move next
    Player currentPlayer;
    AbstractPawn[][] board;
    private final int boardWidth;
    private final int boardHeight;

    // grid n by m, null if empty, WHITE whilst being occupied by a white pawn, BLACK - black pawn
    //PlayerRole[][] board;
    protected Game(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        generateBoard();
        //board = new PlayerRole[boardWidth][boardHeight];
    }

    private void generateBoard() {
        board = new AbstractPawn[boardHeight][boardWidth];
        for (int y = 0; y < boardHeight; y++){
            for (int x = 0; x < boardWidth; x++){
                if (y <= 2 && (x+y) % 2 != 0) {
                    board[x][y] = new Pawn(PawnColor.BLACK); //PlayerRole.BLACK;
                }
                if (y >= boardHeight-3 && (x+y) % 2 != 0) {
                    board[x][y] = new Pawn(PawnColor.WHITE); //PlayerRole.WHITE;
                }
                //System.out.println("board[" + x + "][" + y +"] =  " + board[x][y]);
            }
        }
    }

    public boolean hasWinner() {
        boolean hasBlackPawns = false;
        boolean hasWhitePawns = false;
        for (int y = 0 ; y < boardHeight ; y++) {
            for (int x = 0 ; x < boardWidth ; x++) {
                if (board[x][y] != null) {
                    if (board[x][y].getColor() == PawnColor.WHITE )
                        hasWhitePawns = true;
                    if (board[x][y].getColor() == PawnColor.BLACK )
                        hasBlackPawns = true;
                }
            }
            if (hasWhitePawns && hasBlackPawns) {
                break;
            }
        }

        return !hasWhitePawns || !hasBlackPawns;
    }
    public synchronized void move(String type, int oldX, int oldY, int newX, int newY, int killX, int killY, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        } else if (player.getOpponent() == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        } else if (board[newX][newY] != null) {
            throw new IllegalStateException("Cell already occupied");
        } else if (newX >= boardWidth || newY >= boardHeight) {
            throw new IllegalStateException("Pawns cannot leave the board");
        }
        AbstractPawn pawnToMove = board[oldX][oldY];
        System.out.println("board before:" + Arrays.deepToString(board));
        board[oldX][oldY] = null;
        board[newX][newY] = pawnToMove; //currentPlayer.playerColor;

        if (type.equalsIgnoreCase("KILL")) {
            board[killX][killY] = null;
        }
        System.out.println("board after:" + Arrays.deepToString(board));
        currentPlayer = currentPlayer.getOpponent();
    }


}
