package com.minigolf.puttpoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnerScreen extends AppCompatActivity {

    int currentHole = Game.currentGame.getCurrentHole();
    private final ArrayList<Player> players = Game.currentGame.getPlayers();
    private LinearLayout scorecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);
        populateScoreCardView();
        updateScoreCard();
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

        if (currentHole <= Game.currentGame.getNumHoles()) {
            if (currentHole > Game.currentGame.getCurrentHole())
                return "--";

            int[] playerScores = Game.currentGame.getPlayerScores().get(currentHole - 1);

            if (playerScores[player] != Integer.MIN_VALUE)
                return playerScores[player] + "";
            else
                return "--";
        }
        //for the "total" page
        else {
            int total = 0;

            for (int i = 0; i < Game.currentGame.getNumHoles() - 1; i++) {
                int[] playerScores = Game.currentGame.getPlayerScores().get(i);

                if (playerScores[player] != Integer.MIN_VALUE)
                    total += playerScores[player];
            }
            return total + "";
        }
    }
}