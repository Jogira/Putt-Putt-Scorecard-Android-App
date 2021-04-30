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

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);

        Game.currentGame.setActive(false);
        UserPreferencesManager manager = new UserPreferencesManager(this);
        manager.addGame(Game.currentGame);
        manager.incrementNumGamesPlayed();

        CircleImageView playerIcon = findViewById(R.id.playerImageView);
        TextView winnerOfGame = findViewById(R.id.winnerText);
        Button viewScoreCard = findViewById(R.id.viewScoreCard);
        Button done = findViewById(R.id.exitToHome);
        ArrayList<Player> players = Game.currentGame.getPlayers();
        int winner = Game.currentGame.getWinnerIndex();

        for(int i = 0; i < Game.currentGame.getPlayers().size(); i++) {
            Player p = Game.currentGame.getPlayers().get(i);
            int playerTotal = Game.currentGame.getPlayerTotal(i);

            if(i == winner)
                p.incrementWins();
            else
                p.incrementLosses();

            p.addToPointsScored(playerTotal);
            p.incrementGamesPlayed();

            manager.updatePlayer(p);
        }

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