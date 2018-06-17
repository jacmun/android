package com.example.jackiemun1.minesweeper;

import android.support.design.widget.Snackbar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MinesweeperView extends View {

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintOpen;

    private Bitmap flag = null;
    private Bitmap empty = null;
    private Bitmap bomb = null;
    private Bitmap numOne = null;
    private Bitmap numTwo = null;
    private Bitmap numThree = null;

    private boolean gameEnd = false;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.GRAY);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);

        paintOpen = new Paint();
        paintOpen.setARGB(0, 0, 0, 0);

        flag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        empty = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        numOne = BitmapFactory.decodeResource(getResources(), R.drawable.numone);
        numTwo = BitmapFactory.decodeResource(getResources(), R.drawable.numtwo);
        numThree = BitmapFactory.decodeResource(getResources(), R.drawable.numthree);

        MinesweeperModel.getInstance().generateMines();
        MinesweeperModel.getInstance().checkForMineNeighbors();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);
        drawGameBoard(canvas);
        drawBoardContent(canvas);
        drawBoardState(canvas);
    }

    private void drawGameBoard(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(),
                2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(),
                3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(),
                4 * getHeight() / 5, paintLine);

        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(),
                paintLine);
    }

    private void drawBoardContent(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                float centerX = (i) * getWidth() / 5;
                float centerY = (j) * getHeight() / 5;

                if (MinesweeperModel.getInstance().getFieldContent(i, j) == MinesweeperModel.MINE) {
                    canvas.drawBitmap(bomb, centerX, centerY, null);
                } else if (MinesweeperModel.getInstance().getFieldContent(i, j) == MinesweeperModel.EMPTY) {
                    canvas.drawBitmap(empty, centerX, centerY, null);
                } else if (MinesweeperModel.getInstance().getFieldContent(i, j) == MinesweeperModel.ONE_MINE) {
                    canvas.drawBitmap(numOne, centerX, centerY, null);
                } else if (MinesweeperModel.getInstance().getFieldContent(i, j) == MinesweeperModel.TWO_MINES) {
                    canvas.drawBitmap(numTwo, centerX, centerY, null);
                } else if (MinesweeperModel.getInstance().getFieldContent(i, j) == MinesweeperModel.THREE_MINES) {
                    canvas.drawBitmap(numThree, centerX, centerY, null);
                }
            }
        }
    }


    private void drawBoardState(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                float centerX = (i) * getWidth() / 5;
                float centerY = (j) * getHeight() / 5;

                if (MinesweeperModel.getInstance().getBoardTapState(i, j) == MinesweeperModel.NO_TAP) {
                    canvas.drawRect(centerX, centerY, centerX + (getWidth() / 5), centerY + (getHeight() / 5), paintBg);
                } else if (MinesweeperModel.getInstance().getBoardTapState(i, j) == MinesweeperModel.TAPPED) {
                    canvas.drawRect(centerX, centerY, centerX + getWidth() / 5, centerY + getHeight() / 5, paintOpen);
                } else if (MinesweeperModel.getInstance().getBoardTapState(i, j) == MinesweeperModel.FLAGGED) {
                    canvas.drawRect(centerX, centerY, centerX + getWidth() / 5, centerY + getHeight() / 5, paintBg);
                    canvas.drawBitmap(flag, centerX, centerY, null);
                }

            }
        }
        drawGameBoard(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!gameEnd) {
                int tX = ((int) event.getX()) / (getWidth() / 5);
                int tY = ((int) event.getY()) / (getHeight() / 5);

                updateBoardTapState(tX, tY);
                checkResult();
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    private void updateBoardTapState(int tX, int tY) {
        if (tX < 5 && tY < 5 &&
                MinesweeperModel.getInstance().getBoardTapState(tX, tY) == MinesweeperModel.NO_TAP &&
                MinesweeperModel.getInstance().getStatus() == MinesweeperModel.REVEALED) {
            MinesweeperModel.getInstance().setBoardTapState(tX, tY, MinesweeperModel.TAPPED);
        } else if (tX < 5 && tY < 5 &&
                MinesweeperModel.getInstance().getBoardTapState(tX, tY) == MinesweeperModel.NO_TAP &&
                MinesweeperModel.getInstance().getStatus() == MinesweeperModel.FLAGGED) {
            MinesweeperModel.getInstance().setBoardTapState(tX, tY, MinesweeperModel.FLAGGED);
        }
    }

    private void checkResult() {
        if (MinesweeperModel.getInstance().isGameWon() && !(MinesweeperModel.getInstance().isGameLost())) {
            Snackbar.make(((MainActivity) getContext()).findViewById(R.id.activity_main),
                    R.string.winnerMsg, Snackbar.LENGTH_LONG).show();
            gameEnd = true;
        } else if (MinesweeperModel.getInstance().isGameLost()) {
            Snackbar.make(((MainActivity) getContext()).findViewById(R.id.activity_main),
                    R.string.loserMsg, Snackbar.LENGTH_LONG).show();
            MinesweeperModel.getInstance().showBoard();
            gameEnd = true;
        } else if (!(MinesweeperModel.getInstance().isGameWon()) && !(MinesweeperModel.getInstance().isGameLost())) {

        }
    }

    public void clearBoard() {
        MinesweeperModel.getInstance().resetBoard();
        MinesweeperModel.getInstance().generateMines();
        MinesweeperModel.getInstance().checkForMineNeighbors();
        invalidate();
    }

    public void setGameEnd(boolean isGameOver) {
        gameEnd = isGameOver;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bomb = Bitmap.createScaledBitmap(bomb,
                getWidth() / 5, getHeight() / 5, false);
        empty = Bitmap.createScaledBitmap(empty,
                getWidth() / 5, getHeight() / 5, false);
        numOne = Bitmap.createScaledBitmap(numOne,
                getWidth() / 5, getHeight() / 5, false);
        numTwo = Bitmap.createScaledBitmap(numTwo,
                getWidth() / 5, getHeight() / 5, false);
        numThree = Bitmap.createScaledBitmap(numThree,
                getWidth() / 5, getHeight() / 5, false);
        flag = Bitmap.createScaledBitmap(flag,
                getWidth() / 5, getHeight() / 5, false);
    }

}