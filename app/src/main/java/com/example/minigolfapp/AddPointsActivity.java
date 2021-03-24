package com.example.minigolfapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;
import static java.sql.Types.NULL;


public class AddPointsActivity extends AppCompatActivity {

    private TextView scoreToAdd;
    private TextView currentHoleTextView;
    private final String fileName = Game.currentGame.getFileName();
    private int currentPlayerTurn = Game.currentGame.currentPlayerTurn;
    private TextView currentPlayerName;
    private LinearLayout playerIconView;
    private boolean parsOn = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

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
        playerIconView = findViewById(R.id.playerIconView);
        currentPlayerName = findViewById(R.id.gameViewPlayerTurnTextView);

        currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
        currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s turn");
        currentPlayerTurn = Game.currentGame.currentPlayerTurn;

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementScore();
                AnimationController.buttonPress(AddPointsActivity.this, view);
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementScore();
                AnimationController.buttonPress(AddPointsActivity.this, view);
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
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                Game.currentGame.setPlayerScore(currentPlayerTurn, Integer.parseInt((String) scoreToAdd.getText()));

//                String lines = "";
//                StringBuilder newStr = new StringBuilder();
//
//                Log.d(TAG, "Input: " + Game.currentGame.getCurrentHole());
//                newStr.append(Game.currentGame.getCurrentHole()).append(",").append(scoreToAdd.getText()).append("\n");
//                Log.d(TAG, "Output:" + newStr);
//
//                try {
//
//                    FileOutputStream out = openFileOutput(fileName, Context.MODE_APPEND);
//                    out.write(newStr.toString().getBytes());
//                    out.close();
//
//                    FileInputStream fileInputStream = openFileInput(fileName);
//                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//
//                    BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
//                    StringBuffer stringBuffer = new StringBuffer();
//
//                    while ((lines = bufferedReader.readLine()) != null)
//                        stringBuffer.append(lines).append("\n");
//
//                    //Log.d(TAG, "Reach here:" + stringBuffer.toString());
//                    inputStreamReader.close();
//                }
//                catch (IOException e) { e.printStackTrace(); }

                incrementPlayerTurn();
            }

        });

        openCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                openScorecard(false);
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                openScorecard(true);
            }
        });

        populatePlayerIconView();
    }


    public void incrementPlayerTurn() {
        int numPlayers = Game.currentGame.getPlayers().size();


        if(holeFinished()){
            if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
                Game.currentGame.setActive(false);
                openScorecard(true);
            }
            else {
                Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole() + 1);
                currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
                currentPlayerTurn = 0;
            }
        }
        else if(currentPlayerTurn < numPlayers - 1)
            currentPlayerTurn++;

        else {
                Context context = getApplicationContext();
                CharSequence text = "Must add score to card for ALL players!!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            currentPlayerTurn = 0;
        }

        updatePlayerTurn(currentPlayerTurn);
    }

    public boolean holeFinished(){
        boolean finished = true;
        int[] currentHole = Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole() - 1);
        for(int x : currentHole){
            if(x == Integer.MIN_VALUE){
                finished = false;
            }
        }
        return finished;
    }

    //initial population of the player profile views in the top of the screen
    private void populatePlayerIconView(){
        playerIconView.removeAllViews();
        for(int i = 0; i < Game.currentGame.getPlayers().size(); i++) {

            final CircleImageView playerImageView = new CircleImageView(this);
            playerImageView.setImageDrawable(Game.currentGame.getPlayers().get(i).getPlayerProfileImage());
            playerImageView.setTag(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = 140;
            params.height = 140;
            params.setMarginStart(20);
            params.setMarginEnd(20);
            params.gravity = Gravity.CENTER_VERTICAL;
            playerImageView.setBorderColor(Color.parseColor("#1CD371"));
            playerImageView.setBorderWidth(0);

            if(i == Game.currentGame.currentPlayerTurn) {
                playerImageView.setBorderWidth(8);
                params.height = 170;
                params.width = 170;
            }

            playerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    updatePlayerTurn((Integer) v.getTag());
                }
            });
            playerIconView.addView(playerImageView, params);
        }
    }


    //updates view of players in top, also updates currentPlayerTurn int and Game.currentPlayerTurn
    private void updatePlayerTurn(int index) {
        for(int i = 0; i < playerIconView.getChildCount(); i++) {
            CircleImageView playerProfile = (CircleImageView) playerIconView.getChildAt(i);
            if(i == index) {
                    playerProfile.setBorderWidth(8);
                    currentPlayerTurn = i;
                    Game.currentGame.currentPlayerTurn = currentPlayerTurn;
                    currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s turn");
                    AnimationController.playAnimation(this, playerProfile, R.anim.scale_up);
                    playerProfile.getLayoutParams().height = 170;
                    playerProfile.getLayoutParams().width = 170;
            }
            else {
                playerProfile.setBorderWidth(0);
                AnimationController.playAnimation(this, playerProfile, R.anim.scale_down);
                playerProfile.getLayoutParams().height = 140;
                playerProfile.getLayoutParams().width = 140;
            }
            playerProfile.requestLayout();
        }
    }


    private void incrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score++;
        String incrementedScore = valueOf(score);
        scoreToAdd.setText(incrementedScore);
    }


    private void decrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        if((score - 1) >= 0 || parsOn)
            score--;
        String decrementedScore = valueOf(score);
        scoreToAdd.setText(decrementedScore);
    }


    private void openScorecard(boolean gameFinished) {
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        scorecard.putExtra("gameFinished", gameFinished);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
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