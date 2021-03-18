package com.example.minigolfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScoreCardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreCardActivity";
    int currentHole = Game.currentGame.getCurrentHole();
    private String filename;
    private final ArrayList<Player> players = Game.currentGame.getPlayers();
    private boolean inEditMode = false;
    private LinearLayout scorecard;
    private TextView holeNumberView;
    private boolean gameFinished;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorecard_activity);

        filename = Game.currentGame.getFileName();
        final Button scoreCardEditButton = findViewById(R.id.scorecardEditButton);
        final Button doneButton = findViewById(R.id.exitScorecardButton);
        scorecard = findViewById(R.id.scorecardPlayerView);
        holeNumberView = findViewById(R.id.holeTitleTextView);
        SeekBar holeSeekBar = findViewById(R.id.holeSeekBar);
        holeSeekBar.setMax(Game.currentGame.getNumHoles());

        gameFinished = getIntent().getBooleanExtra("gameFinished", false);
        holeNumberView.setText("Hole " + currentHole);

        if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
            Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole()+1);
            currentHole = Game.currentGame.getCurrentHole();
            holeNumberView.setText("Total");
        }

        holeSeekBar.setProgress(currentHole-1);

        holeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i < Game.currentGame.getNumHoles())
                    holeNumberView.setText("Hole " + (i + 1));
                else
                    holeNumberView.setText("Total");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentHole = seekBar.getProgress() + 1;
                updateScoreCard();
            }
        });

        scoreCardEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAnimation(view, R.anim.button_press_in,0);
                playAnimation(view, R.anim.button_press_out,100);

                if (inEditMode) {
                    inEditMode = false;
                    scoreCardEditButton.setText("Edit");
                    doneButton.setVisibility(View.VISIBLE);
                }
                else {
                    inEditMode = true;
                    scoreCardEditButton.setText("Editing");
                    doneButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAnimation(view, R.anim.button_press_in,0);
                playAnimation(view, R.anim.button_press_out,100);

                if(gameFinished) {
                    Intent homeScreen = new Intent(ScoreCardActivity.this, MainActivity.class);
                    startActivity(homeScreen);
                    ScoreCardActivity.this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
                }
                else {
                    Intent gameScreen = new Intent(ScoreCardActivity.this, AddPointsActivity.class);
                    startActivity(gameScreen);
                    ScoreCardActivity.this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
                }
            }
        });

        populateScoreCardView();
        updateScoreCard();
    }


    public void updateScoreCard() {

        //the SeekBar calls this method when its value is changed.
        //this method should get current hole value, and pull score info from csv file (or preferably the game object) and update it
        //below is an example on how to change the scores

        for (int i = 0; i < scorecard.getChildCount(); i++) {
            TextView score = scorecard.getChildAt(i).findViewById(R.id.scorecardRowPlayerScore);

            //here, you will fetch scores from csv and update appropriately
            Log.d(TAG, "number sent: " + currentHole);
            score.setText(setScore(currentHole));
        }
    }


    private String setScore(int currentHole) {
        String gameFile = "";

        try {
            String lines = "";
            FileInputStream fileInputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader((inputStreamReader));

            while ((lines = bufferedReader.readLine()) != null)
                stringBuffer.append(lines).append("\n");

            Log.d(TAG, "setScore:" + stringBuffer.toString());
            inputStreamReader.close();
            gameFile = stringBuffer.toString();

        }
        catch (IOException e) { e.printStackTrace(); }

        String[] finder = gameFile.split("\n"); //parses the csv and grabs the number hole it is looking for

        if (currentHole >= finder.length && (currentHole > Game.currentGame.getNumHoles())) {
            int total = 0;
            for(int i = 1; i < finder.length; i++) {
                String score = finder[i];
                String[] stuff = score.split(",");
                int scoreValue = Integer.parseInt(stuff[1]);
                total += scoreValue;
            }
            return String.valueOf(total);
        }

        if (currentHole >= finder.length)
            return "N/A";

        String score = finder[currentHole];
        String[] stuff = score.split(",");
        Log.d(TAG, "number Being inputted:" + currentHole);
        Log.d(TAG, "The line there:" + finder[currentHole]);
        score = stuff[1];
        Log.d(TAG, "The val that is being returned:" + score);
        return score;
    }

    //initial population of scorecard
    public void populateScoreCardView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 25);

        for (int i = 0; i < players.size(); i++) {
            View examplePlayerRow = View.inflate(this, R.layout.scorecard_row, null);
            CircleImageView playerProfile = examplePlayerRow.findViewById(R.id.scorecardRowPlayerImageView);
            playerProfile.setImageDrawable(players.get(i).getPlayerProfileImage());
            TextView playerName = examplePlayerRow.findViewById(R.id.scorecardRowPlayerName);
            TextView playerScore = examplePlayerRow.findViewById(R.id.scorecardRowPlayerScore);

            if (i == 0)
                params.setMargins(0, 20, 0, 25);

            playerName.setText(" " + players.get(i).getName());
            playerScore.setText("N/A");
            scorecard.addView(examplePlayerRow, params);
        }
    }


    private void playAnimation(final View v, final int animationId, int delayMS) {
        if(v != null) {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(ScoreCardActivity.this, animationId);
                    animation.setDuration(animation.getDuration());
                    animation.setFillAfter(true);
                    v.startAnimation(animation);
                }
            }, delayMS);
        }
    }
}