package com.example.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView timerShow, countDecrease;
    private Button tapButton;
    private CountDownTimer countDownTimer;

    private int count = 10;
    private int timeRemaining = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerShow = findViewById(R.id.timerShow);
        countDecrease = findViewById(R.id.countDecrease);
        tapButton = findViewById(R.id.tapButton);

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count");
            timeRemaining = savedInstanceState.getInt("timeRemaining");
//            Log.i("ABC", count + " " + timeRemaining);
            restoreGame();

        }

        countDecrease.setText(String.valueOf(count));

        tapButton.setOnClickListener(v -> {
            if (count > 0) {
                count--;
                countDecrease.setText(String.valueOf(count));
            } else {
                countDownTimer.cancel();
                showDialog("Game Over", "Great Game!");
            }
        });

        if (savedInstanceState == null) {
            countDecrease.setText(String.valueOf(count));
            countDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerShow.setText(String.valueOf(millisUntilFinished / 1000));
                    timeRemaining = (int) millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    timerShow.setText("0");
                    countDecrease.setText("0");
                    showDialog("Time\'s up!", "You have finished the game!");
                }
            };

            countDownTimer.start();
        }
    }

    private void resetGame() {
        count = 10;
        countDecrease.setText(String.valueOf(count));

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) millisUntilFinished;
                timerShow.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerShow.setText("0");
                countDecrease.setText("0");
            }
        };

        countDownTimer.start();
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Reset", (dialog, which) -> resetGame());
        builder.setNegativeButton("Exit", (dialog, which) -> finish());
        builder.show();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
        outState.putInt("timeRemaining", timeRemaining);
        Log.i("ABC", count + " " + timeRemaining);
        countDownTimer.cancel();
    }

    private void restoreGame() {
        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) millisUntilFinished;
                timerShow.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timerShow.setText("0");
                countDecrease.setText("0");
            }
        };

        countDownTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item) {
            Toast.makeText(this, "Game Version: " + BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}