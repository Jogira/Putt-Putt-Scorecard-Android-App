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
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int currentGamePage = 0;
    private static final int PAST_GAMES = 1;
    private static final int ACTIVE_GAMES = 0;
    private Button activeGamesButton;
    private Button pastGamesButton;
    private LinearLayout gamesScrollViewContent;
    private LinearLayout noGamesView;
    private TextView noGamesText;
    private float dp;
    private UserPreferencesManager manager;
    private ArrayList<Game> games;
    private ArrayList<Game> activeGames;
    private ArrayList<Game> pastGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dp = this.getResources().getDimension(R.dimen.pixelsToDP);

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

    private void openScorecardPage() {
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        scorecard.putExtra("gameFinished", true);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }


    private void populateGamesScrollView(final int pageType) {

        gamesScrollViewContent.removeAllViews();
        games = manager.getGames();
        activeGames = new ArrayList<>();
        pastGames = new ArrayList<>();

        for(int i = 0; i < games.size(); i++) {
            if(games.get(i).getActive())
                activeGames.add(games.get(i));
            else if(!games.get(i).getActive())
                pastGames.add(games.get(i));
        }

        if(pageType == ACTIVE_GAMES && activeGames.size() != 0 || pageType == PAST_GAMES && pastGames.size() != 0)
            noGamesView.setVisibility(View.GONE);
        else
            noGamesView.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int)dp * 10;

        int delay = 0;
        if(pageType == ACTIVE_GAMES) {

            for (int i = activeGames.size()-1; i >= 0; i--) {
                final int index = i;
                final View activeGame = View.inflate(this, R.layout.item_active_view, null);
                activeGame.setLayoutParams(params);
                final ImageButton resumeGameButton = activeGame.findViewById(R.id.resumeActiveGameButton);
                final ImageButton deleteGameButton = activeGame.findViewById(R.id.deleteActiveGameButton);

                TextView currentHole = activeGame.findViewById(R.id.activeGameCurrentHoleTextView);
                currentHole.setText(String.valueOf(activeGames.get(i).getCurrentHole()));

                int numPlayers = activeGames.get(i).getPlayers().size();

                CircleImageView playerOneImage = activeGame.findViewById(R.id.activeGamePlayerImage);
                CircleImageView playerTwoImage = activeGame.findViewById(R.id.activeGamePlayer2Image);
                TextView morePlayersView = activeGame.findViewById(R.id.morePlayersView);

                playerOneImage.setImageDrawable(activeGames.get(i).getPlayers().get(0).getPlayerProfileImage(this));

                if(numPlayers < 3)
                    morePlayersView.setVisibility(View.INVISIBLE);
                if(numPlayers < 2)
                    playerTwoImage.setVisibility(View.INVISIBLE);
                else
                    playerTwoImage.setImageDrawable(activeGames.get(i).getPlayers().get(1).getPlayerProfileImage(this));

                resumeGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                        Game.currentGame = activeGames.get(index);
                        openPointsPage();

                    }
                });
                deleteGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, view);
                        manager.removeGame(activeGames.get(index));
                        gamesScrollViewContent.removeView(activeGame);
                        activeGames.remove(index);

                        if(activeGames.size() == 0)
                            noGamesView.setVisibility(View.VISIBLE);
                    }
                });
                gamesScrollViewContent.addView(activeGame);
                AnimationController.playAnimation(MainActivity.this, activeGame, R.anim.quick_zoom, delay);
                delay += 25;
            }
        }
        else {

            for (int i = pastGames.size() - 1; i >= 0; i--) {
                final int index = i;
                View pastGame = View.inflate(this, R.layout.item_history_view, null);
                pastGame.setLayoutParams(params);
                final Button viewScoreCardButton = pastGame.findViewById(R.id.historyGameViewScoreCardButton);
                TableRow playerRowOne = pastGame.findViewById(R.id.playerRowOne);
                TableRow playerRowTwo = pastGame.findViewById(R.id.playerRowTwo);

                int winnerIndex = pastGames.get(i).getWinnerIndex();
                CircleImageView winnerImage = pastGame.findViewById(R.id.historyGameWinnerPicture);
                winnerImage.setImageDrawable(pastGames.get(i).getPlayers().get(winnerIndex).getPlayerProfileImage(this));

                TextView winnerTitle = pastGame.findViewById(R.id.historyGameWinnerTitle);
                winnerTitle.setText(pastGames.get(i).getPlayers().get(winnerIndex).getName() + " Won!");

                viewScoreCardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationController.buttonPress(MainActivity.this, viewScoreCardButton);
                        Game.currentGame = pastGames.get(index);
                        openScorecardPage();
                    }
                });

                for(int j = 0; j < pastGames.get(i).getPlayers().size(); j++) {
                    LinearLayout playerScoreView = (LinearLayout) View.inflate(this, R.layout.item_history_score_view, null);

                    CircleImageView playerProfileImage = playerScoreView.findViewById(R.id.historyPlayerProfile);
                    TextView playerScore = playerScoreView.findViewById(R.id.historyPlayerScore);
                    playerScore.setText(String.valueOf(pastGames.get(i).getPlayerTotal(j)) + "  ");
                    playerProfileImage.setImageDrawable(pastGames.get(i).getPlayers().get(j).getPlayerProfileImage(this));

                    if(j < 4)
                        playerRowOne.addView(playerScoreView);
                    else if(j < 8)
                        playerRowTwo.addView(playerScoreView);
                }

                gamesScrollViewContent.addView(pastGame);
                AnimationController.playAnimation(MainActivity.this, pastGame, R.anim.quick_zoom, delay);
                delay += 25;
            }
        }

    }

}