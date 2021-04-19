package com.minigolf.puttpoints;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
    private int previousHole = 0;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorecard_activity);

        final Button scoreCardEditButton = findViewById(R.id.scorecardEditButton);
        final Button doneButton = findViewById(R.id.exitScorecardButton);
        final Button viewTotals = findViewById(R.id.viewTotalsButton);
        scorecard = findViewById(R.id.scorecardPlayerView);
        holeNumberView = findViewById(R.id.holeTitleTextView);
        final SeekBar holeSeekBar = findViewById(R.id.holeSeekBar);
        holeSeekBar.setMax(Game.currentGame.getNumHoles());

        gameFinished = getIntent().getBooleanExtra("gameFinished", false);
        holeNumberView.setText("Hole " + currentHole);

        if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
            Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole()+1);
            currentHole = Game.currentGame.getCurrentHole();
            holeNumberView.setText("Total  ");
        }

        holeSeekBar.setProgress(currentHole-1);

        holeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i < Game.currentGame.getNumHoles())
                    holeNumberView.setText("Hole " + (i + 1));
                else
                    holeNumberView.setText("Total  ");

                viewTotals.setText("View Totals");
                currentHole = seekBar.getProgress() + 1;
                updateScoreCard();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        scoreCardEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(ScoreCardActivity.this, view);

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

        scoreCardEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(ScoreCardActivity.this, view);

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
                AnimationController.buttonPress(ScoreCardActivity.this, view);

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

        viewTotals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(ScoreCardActivity.this, view);

                if(currentHole == 19) {
                    currentHole = previousHole;
                    holeSeekBar.setProgress(currentHole-1);
                    viewTotals.setText("View Totals");
                }
                else {
                    previousHole = currentHole;
                    currentHole = 19;
                    holeSeekBar.setProgress(currentHole);
                    viewTotals.setText("Hole " + previousHole);
                }
                updateScoreCard();
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
            score.setText(setScore(currentHole, i));
        }
    }


    private String setScore(int currentHole, int player) {
        String gameFile = "";

        if(currentHole <= Game.currentGame.getNumHoles()) {
            if(currentHole > Game.currentGame.getCurrentHole())
                return "--";

            int[] playerScores = Game.currentGame.getPlayerScores().get(currentHole-1);

            if (playerScores[player] != Integer.MIN_VALUE)
                return playerScores[player] + "";
            else
                return "--";
        }
        //for the "total" page
        else {
            int total = 0;

            for(int i = 0; i < Game.currentGame.getNumHoles(); i++){
                int[] playerScores = Game.currentGame.getPlayerScores().get(i);

                if (playerScores[player] != Integer.MIN_VALUE)
                    total += playerScores[player];
            }
            return total+"";
        }

//        try {
//            String lines = "";
//            FileInputStream fileInputStream = openFileInput(filename);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            StringBuilder stringBuffer = new StringBuilder();
//            BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
//
//            while ((lines = bufferedReader.readLine()) != null)
//                stringBuffer.append(lines).append("\n");
//
//            Log.d(TAG, "setScore:" + stringBuffer.toString());
//            inputStreamReader.close();
//            gameFile = stringBuffer.toString();
//
//        }
//        catch (IOException e) { e.printStackTrace(); }
//
//        String[] finder = gameFile.split("\n"); //parses the csv and grabs the number hole it is looking for
//
//        if (currentHole >= finder.length && (currentHole > Game.currentGame.getNumHoles())) {
//            int total = 0;
//            for(int i = 1; i < finder.length; i++) {
//                String score = finder[i];
//                String[] stuff = score.split(",");
//                int scoreValue = Integer.parseInt(stuff[1]);
//                total += scoreValue;
//            }
//            return String.valueOf(total);
//        }
//
//        if (currentHole >= finder.length)
//            return "N/A";
//
//        String score = finder[currentHole];
//        String[] stuff = score.split(",");
//        Log.d(TAG, "number Being inputted:" + currentHole);
//        Log.d(TAG, "The line there:" + finder[currentHole]);
//        score = stuff[1];
//        Log.d(TAG, "The val that is being returned:" + score);
//        return score;
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

}