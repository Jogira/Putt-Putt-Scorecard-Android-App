package com.minigolf.puttpoints;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnerScreenActivity extends AppCompatActivity {

    private ArrayList<Player> players = Game.currentGame.getPlayers();

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);

        CircleImageView playerIcon = findViewById(R.id.playerImageView);
        TextView winnerOfGame = findViewById(R.id.winnerText);
        Button viewScoreCard = findViewById(R.id.viewScoreCard);
        Button done = findViewById(R.id.exitToHome);

        int winner = getWinnerIndex();

        winnerOfGame.setText(players.get(winner).getName() + " Won!!!");
        playerIcon.setImageDrawable(players.get(winner).getPlayerProfileImage(this));

        viewScoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(WinnerScreenActivity.this, view);
                openScorecard();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(WinnerScreenActivity.this, view);
                goHome();
            }
        });

    }


    public int getWinnerIndex() {
        int total = 0;
        ArrayList<Integer> playerTotals = new ArrayList<>();

        for(int player = 0; player < Game.currentGame.getPlayers().size(); player++) {
            for (int hole = 0; hole < Game.currentGame.getNumHoles();  hole++) {
                int[] playerScores = Game.currentGame.getPlayerScores().get(hole);

                if (playerScores[player] != Integer.MIN_VALUE)
                    total += playerScores[player];
            }
            playerTotals.add(total);
            total = 0;
        }

        int lowestScore = Integer.MAX_VALUE;
        int winnerIndex = 0;
        for(int i = 0; i < playerTotals.size(); i++){
            if(playerTotals.get(i) < lowestScore) {
                lowestScore = playerTotals.get(i);
                winnerIndex = i;
            }
            System.out.println(winnerIndex + " " +  Game.currentGame.getPlayers().get(winnerIndex).getName());
        }

        return winnerIndex;
    }


    private void openScorecard() {
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        scorecard.putExtra("gameFinished", true);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
    }


    private void goHome(){
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
    }

}