package com.example.jackiemun1.minesweeper;

import java.util.Random;

public class MinesweeperModel {

    private static MinesweeperModel instance = null;
    public static final int MINE_NUM = 3;
    public static final int BOARD_ROWS = 5;
    public static final int BOARD_COLS = 5;
    Random rand = new Random();

    private MinesweeperModel() {
    }

    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    public static final short EMPTY = 0;
    public static final short MINE = 1;
    public static final short ONE_MINE = 2;
    public static final short TWO_MINES = 3;
    public static final short THREE_MINES = 4;
    public static final short REVEALED = 5;
    public static final short FLAGGED = 6;
    public static final short TAPPED = 7;
    public static final short NO_TAP = 8;


    private short[][] board = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };

    private short[][] boardTapState = {
            {NO_TAP, NO_TAP, NO_TAP, NO_TAP, NO_TAP},
            {NO_TAP, NO_TAP, NO_TAP, NO_TAP, NO_TAP},
            {NO_TAP, NO_TAP, NO_TAP, NO_TAP, NO_TAP},
            {NO_TAP, NO_TAP, NO_TAP, NO_TAP, NO_TAP},
            {NO_TAP, NO_TAP, NO_TAP, NO_TAP, NO_TAP}
    };

    private short status = REVEALED;

    public short getStatus() {
        return status;
    }

    public void setStatusFlagged() {
        status = FLAGGED;
    }

    public void setStatusRevealed() {
        status = REVEALED;
    }

    public void generateMines() {
        for (int i = 0; i < MINE_NUM; i++) {
            int bombX = rand.nextInt(BOARD_ROWS);
            int bombY = rand.nextInt(BOARD_COLS);

            if (board[bombX][bombY] == MINE) {
                i--;
            } else {
                board[bombX][bombY] = MINE;
            }
        }
    }

    public void resetBoard() {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                board[i][j] = EMPTY;
                boardTapState[i][j] = NO_TAP;
            }
        }
        status = REVEALED;
    }

    public short getFieldContent(int row, int col) {
        return board[row][col];
    }

    public short getBoardTapState(int row, int col) {
        return boardTapState[row][col];
    }

    public short setBoardTapState(int row, int col, short content) {
        return boardTapState[row][col] = content;
    }

    public void checkForMineNeighbors() {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                if (board[i][j] != MINE) {

                    int mineNeighbors = 0;

                    if (i < 4 && j < 4 && board[i + 1][j + 1] == MINE) {
                        mineNeighbors++;
                    }
                    if (i > 0 && j > 0 && board[i - 1][j - 1] == MINE) {
                        mineNeighbors++;
                    }
                    if (i < 4 && j > 0 && board[i + 1][j - 1] == MINE) {
                        mineNeighbors++;
                    }
                    if (i > 0 && j < 4 && board[i - 1][j + 1] == MINE) {
                        mineNeighbors++;
                    }
                    if (i < 4 && board[i + 1][j] == MINE) {
                        mineNeighbors++;
                    }
                    if (i > 0 && board[i - 1][j] == MINE) {
                        mineNeighbors++;
                    }
                    if (j < 4 && board[i][j + 1] == MINE) {
                        mineNeighbors++;
                    }
                    if (j > 0 && board[i][j - 1] == MINE) {
                        mineNeighbors++;
                    }
                    board[i][j] = convertToMineStatus(mineNeighbors);
                }
            }
        }
    }

    private short convertToMineStatus(int x) {
        if (x == 1) {
            return ONE_MINE;
        } else if (x == 2) {
            return TWO_MINES;
        } else if (x == 3) {
            return THREE_MINES;
        } else {
            return EMPTY;
        }
    }

    private boolean isMineHit() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == MINE && boardTapState[i][j] == TAPPED) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFlagRight() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] != MINE && boardTapState[i][j] == FLAGGED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameLost() {
        return isMineHit() || isFlagRight();
    }

    public boolean isGameWon() {

        int playerMines = 0;
        int playerTapped = 0;

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                if (board[i][j] == MINE && boardTapState[i][j] == FLAGGED) {
                    playerMines++;
                }
                if (board[i][j] != MINE && boardTapState[i][j] == TAPPED) {
                    playerTapped++;
                }
            }
        }
        return playerMines == MINE_NUM && playerTapped == (BOARD_ROWS * BOARD_COLS) - MINE_NUM;
    }

    public void showBoard() {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                boardTapState[i][j] = TAPPED;
            }
        }
    }

}