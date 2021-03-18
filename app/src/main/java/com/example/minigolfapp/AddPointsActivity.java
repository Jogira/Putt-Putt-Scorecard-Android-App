package com.example.minigolfapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;


public class AddPointsActivity extends AppCompatActivity {

    private TextView scoreToAdd;
    private TextView currentHoleTextView;
    private String fileName;
    private int currentPlayerTurn = Game.currentGame.currentPlayerTurn;
    private CircleImageView currentPlayerImage;
    private TextView currentPlayerName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

        fileName = Game.currentGame.getFileName();

        currentHoleTextView = findViewById(R.id.CurrentHole);
        ImageButton increment = findViewById(R.id.incrementButton);
        ImageButton decrement = findViewById(R.id.decrementButton);
        ImageButton home = findViewById(R.id.homePageButton);
        ImageButton statsPage = findViewById(R.id.statsPageButton);
        Button addScore = findViewById(R.id.addScoreButton);
        scoreToAdd = findViewById(R.id.scoreToAdd);
        Button openCard = findViewById(R.id.viewCard);
        Button endGame = findViewById(R.id.endGame);
        CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
        currentPlayerImage = findViewById(R.id.gameViewPlayerTurnImageView);
        currentPlayerName = findViewById(R.id.gameViewPlayerTurnTextView);

        currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
        currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s turn");
        currentPlayerImage.setImageDrawable(Game.currentGame.getPlayers().get(currentPlayerTurn).getPlayerProfileImage());

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementScore();
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementScore();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHomeScreen();
            }
        });

        statsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStatsPage();
            }
        });

        settingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSettingsPage();
            }
        });

        addScore.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "AddPointsActivity";

            @Override
            public void onClick(View view) {
                String lines = "";
                StringBuilder newStr = new StringBuilder();

                Log.d(TAG, "Set at: " + Game.currentGame.getCurrentHole());
                newStr.append(Game.currentGame.getCurrentHole()).append(",").append(scoreToAdd.getText()).append("\n");
                Log.d(TAG, "Test:" + newStr);

                try {

                    FileOutputStream out = openFileOutput(fileName, Context.MODE_APPEND);
                    out.write(newStr.toString().getBytes());
                    out.close();

                    FileInputStream fileInputStream = openFileInput(fileName);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                    BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
                    StringBuffer stringBuffer = new StringBuffer();

                    while ((lines = bufferedReader.readLine()) != null)
                        stringBuffer.append(lines).append("\n");

                    Log.d(TAG, "Reach here:" + stringBuffer.toString());
                    inputStreamReader.close();
                }
                catch (IOException e) { e.printStackTrace(); }

                incrementPlayerTurn();
            }

        });

        openCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScorecard(false, Game.currentGame.getCurrentHole());
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScorecard(true, Game.currentGame.getCurrentHole());
            }
        });
    }


    public void incrementPlayerTurn() {
        int numPlayers = Game.currentGame.getPlayers().size();

        if(currentPlayerTurn < numPlayers - 1)
            currentPlayerTurn++;

        else {
            if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
                Game.currentGame.setActive(false);
                openScorecard(true, Game.currentGame.getCurrentHole());
            }
            else {
                Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole() + 1);
                currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
            }

            currentPlayerTurn = 0;
        }

        Game.currentGame.currentPlayerTurn = currentPlayerTurn;
        currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s turn");
        currentPlayerImage.setImageDrawable(Game.currentGame.getPlayers().get(currentPlayerTurn).getPlayerProfileImage());
    }


    private void incrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score++;
        String incrementedScore = valueOf(score);
        scoreToAdd.setText(incrementedScore);
    }


    private void decrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score--;
        String decrementedScore = valueOf(score);
        scoreToAdd.setText(decrementedScore);
    }


    private void openScorecard(boolean gameFinished, int currentHole) {
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        scorecard.putExtra("fileName", fileName);
        scorecard.putExtra("gameFinished", gameFinished);
        scorecard.putExtra("currentHole", currentHole);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    private void toHomeScreen() {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    private void toSettingsPage() {
        Intent settingsScreen = new Intent(this, SettingsActivity.class);
        startActivity(settingsScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    private void toStatsPage() {
        Intent statsScreen = new Intent(this, StatsActivity.class);
        startActivity(statsScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

}