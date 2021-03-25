package com.example.minigolfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int currentGamePage = 0;
    private int numActiveGames = 2;
    private int numPastGames = 6;
    private static final int PAST_GAMES = 1;
    private static final int ACTIVE_GAMES = 0;
    private Button activeGamesButton;
    private Button pastGamesButton;
    private LinearLayout gamesScrollViewContent;
    private LinearLayout noGamesView;
    private TextView noGamesText;
    private static boolean playersCreated = false;
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton statsButton = findViewById(R.id.statsPageButton);
        CircleImageView settingsButton = findViewById(R.id.settingsPageButton);
        activeGamesButton = findViewById(R.id.activeGamesButton);
        pastGamesButton = findViewById(R.id.pastGamesButton);
        gamesScrollViewContent = findViewById(R.id.gamesScrollViewContent);
        noGamesView = findViewById(R.id.noGamesView);
        noGamesText = findViewById(R.id.noGamesText);
        Button newGameButton = findViewById(R.id.newGameButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(MainActivity.this, view);
                openNewGamePage();
            }
        });

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

        if(!playersCreated)
            createDefaultPlayers();
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

    private void openNewGamePage(){
        Intent newGamePage = new Intent(this, AddPlayersActivity.class);
        startActivity(newGamePage);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
    }

    //just for demo purposes until we have save functionality
    public void createDefaultPlayers() {
        playersCreated = true;
        Drawable profileImage = getDrawable(R.drawable.sean_kingston_profile);
        String playerName = "Sean";
        Player sean = new Player(playerName, profileImage);

        Drawable profileImage2 = getDrawable(R.drawable.sage_thompson_profile);
        String playerName2 = "Sage";
        Player sage = new Player(playerName2, profileImage2);

        Drawable profileImage3 = getDrawable(R.drawable.ic_person);
        String playerName3 = "John";
        Player john = new Player(playerName3, profileImage3);

        Drawable profileImage4 = getDrawable(R.drawable.doge);
        String playerName4 = "Glue";
        Player doge = new Player(playerName4, profileImage4);

        Player.players.add(sean);
        Player.players.add(sage);
        Player.players.add(john);
        Player.players.add(doge);
    }

    private void populateGamesScrollView(int pageType){

        gamesScrollViewContent.removeAllViews();

        if(pageType == ACTIVE_GAMES && numActiveGames != 0 || pageType == PAST_GAMES && numPastGames != 0)
            noGamesView.setVisibility(View.GONE);
        else
            noGamesView.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 25;

        int delay = 0;
        if(pageType == ACTIVE_GAMES) {
            //example active game
            for (int i = 0; i < numActiveGames; i++) {
                View exampleActiveGame = View.inflate(this, R.layout.item_active_view, null);
                exampleActiveGame.setLayoutParams(params);
                final ImageButton resumeGameButton = exampleActiveGame.findViewById(R.id.resumeActiveGameButton);
                final ImageButton deleteGameButton = exampleActiveGame.findViewById(R.id.deleteActiveGameButton);
                resumeGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                    }
                });
                deleteGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                    }
                });
                gamesScrollViewContent.addView(exampleActiveGame);
                AnimationController.playAnimation(MainActivity.this, exampleActiveGame, R.anim.quick_zoom, delay);
                delay += 25;
            }
        }
        else {
            //example history game
            for (int i = 0; i < numPastGames; i++) {
                View examplePastGame = View.inflate(this, R.layout.item_history_view, null);
                examplePastGame.setLayoutParams(params);
                final Button viewScoreCardButton = examplePastGame.findViewById(R.id.historyGameViewScoreCardButton);
                viewScoreCardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, viewScoreCardButton);
                    }
                });
                gamesScrollViewContent.addView(examplePastGame);
                AnimationController.playAnimation(MainActivity.this, examplePastGame, R.anim.quick_zoom, delay);
                delay += 25;
            }
        }

    }

}