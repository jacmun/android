package com.example.jackiemun1.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MinesweeperView minesweeperView = findViewById(R.id.minesweeper);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnFlag = findViewById(R.id.btnFlag);
        Button btnTry = findViewById(R.id.btnTry);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minesweeperView.clearBoard();
                minesweeperView.setGameEnd(false);
                Toast.makeText(MainActivity.this, R.string.newGameMsg, Toast.LENGTH_SHORT).show();
            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinesweeperModel.getInstance().setStatusFlagged();
                Toast.makeText(MainActivity.this, R.string.flagOnMsg, Toast.LENGTH_SHORT).show();
            }
        });

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinesweeperModel.getInstance().setStatusRevealed();
                Toast.makeText(MainActivity.this, R.string.tryOnMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
