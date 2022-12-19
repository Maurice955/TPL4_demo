package com.example.demo.CheckersServerDemo;

public class ClassicCheckers extends Game {

    public ClassicCheckers() {
        super(8, 8);
    }

    //sth is wrong here
    @Override
    public boolean canMove(int x, int y) {
        if (board[x][y] == null)
            return false;

        if (board[x][y] instanceof Pawn) {
            Pawn pawn = (Pawn) board[x][y];
            if (onBoard(x + pawn.getDir(), y-1)) {
                if (board[x+pawn.getDir()][y-1] == null)
                    return true;
                else if (onBoard(x + 2 * pawn.getDir(), y-2)) {
                    if (board[x + 2 * pawn.getDir()][y - 2] == null)
                        return true;
                }
            }
            if (onBoard(x + pawn.getDir(), y+1)) {
                if (board[x+pawn.getDir()][y+1] == null)
                    return true;
                else if (onBoard(x + 2 * pawn.getDir(), y+2)) {
                    if (board[x + 2 * pawn.getDir()][y + 2] == null)
                        return true;
                }
            }
        }
        else { //King
        }
        return false;
    }

    @Override
    public boolean noMovesPossible(PawnColor color) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (board[i][j] != null && board[i][j].getColor() == color && canMove(i, j))
                    return false;
            }
        }
        return true;
    }
}

