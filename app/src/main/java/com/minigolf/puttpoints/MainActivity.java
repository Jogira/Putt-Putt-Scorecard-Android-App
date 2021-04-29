package com.minigolf.puttpoints;

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


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int currentGamePage = 0;
    private int numActiveGames = 0;
    private int numPastGames = 0;
    private static final int PAST_GAMES = 1;
    private static final int ACTIVE_GAMES = 0;
    private Button activeGamesButton;
    private Button pastGamesButton;
    private LinearLayout gamesScrollViewContent;
    private LinearLayout noGamesView;
    private TextView noGamesText;
    private static boolean playersCreated = false;
    private float dp;
    private UserPreferencesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dp = this.getResources().getDimension(R.dimen.pixelsToDP);

        if(Game.currentGame != null) {
            Game.currentGame.setActive(false);
            manager.addGame(Game.currentGame);
        }
        Game.currentGame = null;
        ImageButton statsButton = findViewById(R.id.statsPageButton);
        CircleImageView settingsButton = findViewById(R.id.settingsPageButton);
        activeGamesButton = findViewById(R.id.activeGamesButton);
        pastGamesButton = findViewById(R.id.pastGamesButton);
        gamesScrollViewContent = findViewById(R.id.gamesScrollViewContent);
        noGamesView = findViewById(R.id.noGamesView);
        noGamesText = findViewById(R.id.noGamesText);
        Button newGameButton = findViewById(R.id.newGameButton);
        manager = new UserPreferencesManager(this);
        settingsButton.setImageDrawable(manager.getPlayers().get(0).getPlayerProfileImage(this));

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

    private void openPointsPage(){
        Intent addPointsPage = new Intent(this, AddPointsActivity.class);
        startActivity(addPointsPage);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }


    private void populateGamesScrollView(final int pageType) {

        gamesScrollViewContent.removeAllViews();
        final ArrayList<Game> games = manager.getGames();

        for(int i = 0; i < games.size(); i++) {
            if(games.get(i).getActive())
                numActiveGames++;
            else
                numPastGames++;
        }

        if(pageType == ACTIVE_GAMES && numActiveGames != 0 || pageType == PAST_GAMES && numPastGames != 0)
            noGamesView.setVisibility(View.GONE);
        else
            noGamesView.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int)dp * 10;

        int delay = 0;
        if(pageType == ACTIVE_GAMES) {
            //example active game
            for (int i = 0; i < numActiveGames; i++) {
                final int index = i;
                final View activeGame = View.inflate(this, R.layout.item_active_view, null);
                activeGame.setLayoutParams(params);
                final ImageButton resumeGameButton = activeGame.findViewById(R.id.resumeActiveGameButton);
                final ImageButton deleteGameButton = activeGame.findViewById(R.id.deleteActiveGameButton);

                int numPlayers = games.get(i).getPlayers().size();

                CircleImageView playerOneImage = activeGame.findViewById(R.id.activeGamePlayerImage);
                CircleImageView playerTwoImage = activeGame.findViewById(R.id.activeGamePlayer2Image);
                TextView morePlayersView = activeGame.findViewById(R.id.morePlayersView);

                playerOneImage.setImageDrawable(games.get(i).getPlayers().get(0).getPlayerProfileImage(this));

                if(numPlayers < 3)
                    morePlayersView.setVisibility(View.INVISIBLE);
                if(numPlayers < 2)
                    playerTwoImage.setVisibility(View.INVISIBLE);
                else
                    playerTwoImage.setImageDrawable(games.get(i).getPlayers().get(1).getPlayerProfileImage(this));

                resumeGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                        openPointsPage();
                        Game.currentGame = games.get(index);
                    }
                });
                deleteGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                        manager.removeGame(games.get(index));
                        gamesScrollViewContent.removeView(activeGame);
                        numActiveGames--;

                        if(numActiveGames == 0)
                            noGamesView.setVisibility(View.GONE);
                    }
                });
                gamesScrollViewContent.addView(activeGame);
                AnimationController.playAnimation(MainActivity.this, activeGame, R.anim.quick_zoom, delay);
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