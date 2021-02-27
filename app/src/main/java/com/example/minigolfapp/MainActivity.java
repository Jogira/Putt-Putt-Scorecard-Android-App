package com.example.minigolfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int currentGamePage = 0;
    private int numActiveGames = 1;
    private int numPastGames = 1;
    private static final int PAST_GAMES = 1;
    private static final int ACTIVE_GAMES = 0;
    private ImageButton statsButton;
    private CircleImageView settingsButton;
    private Button activeGamesButton;
    private Button pastGamesButton;
    private LinearLayout gamesScrollViewContent;
    private LinearLayout noGamesView;
    private TextView noGamesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statsButton = findViewById(R.id.statsPageButton);
        settingsButton = findViewById(R.id.settingsPageButton);
        activeGamesButton = findViewById(R.id.activeGamesButton);
        pastGamesButton = findViewById(R.id.pastGamesButton);
        gamesScrollViewContent = findViewById(R.id.gamesScrollViewContent);
        noGamesView = findViewById(R.id.noGamesView);
        noGamesText = findViewById(R.id.noGamesText);

        activeGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentGamePage != ACTIVE_GAMES)
                    populateGamesScrollView(ACTIVE_GAMES);

                currentGamePage = ACTIVE_GAMES;
                activeGamesButton.getBackground().setTint(Color.parseColor("#2F2F2F"));
                pastGamesButton.getBackground().setTint(Color.parseColor("#141414"));
                noGamesText.setText("You have no active games :/");
            }
        });

        pastGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentGamePage != PAST_GAMES)
                    populateGamesScrollView(PAST_GAMES);

                currentGamePage = PAST_GAMES;
                activeGamesButton.getBackground().setTint(Color.parseColor("#141414"));
                pastGamesButton.getBackground().setTint(Color.parseColor("#2F2F2F"));
                noGamesText.setText("Your history is empty :/");
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatsPage();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsPage();
            }
        });

        populateGamesScrollView(ACTIVE_GAMES);
    }

    private void openStatsPage(){
        Intent statsPage = new Intent(this, StatsActivity.class);
        startActivity(statsPage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void openSettingsPage(){
        Intent settingsPage = new Intent(this, SettingsActivity.class);
        startActivity(settingsPage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void populateGamesScrollView(int pageType){

        gamesScrollViewContent.removeAllViews();

        if(pageType == ACTIVE_GAMES && numActiveGames != 0 || pageType == PAST_GAMES && numPastGames != 0)
            noGamesView.setVisibility(View.GONE);
        else
            noGamesView.setVisibility(View.VISIBLE);

        if(pageType == ACTIVE_GAMES) {

            //example active game
            View exampleActiveGame = View.inflate(this, R.layout.item_active_view, null);
            TextView currentHole = exampleActiveGame.findViewById(R.id.activeGameCurrentHoleTextView);
            currentHole.setText("3");
            gamesScrollViewContent.addView(exampleActiveGame);
        }
        else {

            //example history game
            View examplePastGame = View.inflate(this, R.layout.item_history_view, null);
            examplePastGame.setMinimumHeight(200);
            gamesScrollViewContent.addView(examplePastGame);
        }

    }

}