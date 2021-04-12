package com.minigolf.puttpoints;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnerScreen extends AppCompatActivity {

    private ArrayList<Player> players = Game.currentGame.getPlayers();
    private CircleImageView playerIcon;
    private TextView winnerOfGame;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);

        playerIcon = findViewById(R.id.playerImageView);
        winnerOfGame = findViewById(R.id.winnerOfGame);

        int winner = getWinnerIndex();

        winnerOfGame.setText(players.get(winner).getName() + " Wins!!!");
        playerIcon.setImageDrawable(players.get(winner).getPlayerProfileImage());



    }

    public int getWinnerIndex() {
        int total = 0;
        ArrayList<Integer> playerTotals = new ArrayList<>();

        for(int player = 0; player < Player.players.size(); player++) {
            for (int hole = 0; hole < Game.currentGame.getNumHoles() - 1;  hole++) {
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
        }

        return winnerIndex;
    }

}