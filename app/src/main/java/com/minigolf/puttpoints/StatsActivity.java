package com.minigolf.puttpoints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class StatsActivity extends AppCompatActivity {


    private TableLayout statsPageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        UserPreferencesManager manager = new UserPreferencesManager(this);

        Player user = manager.getPlayers().get(0);

        ImageButton homeButton = findViewById(R.id.homePageButton);
        CircleImageView settingsButton = findViewById(R.id.settingsPageButton);
        settingsButton.setImageDrawable(user.getPlayerProfileImage(this));
        statsPageContent = findViewById(R.id.statsPageContent);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsPage();
            }
        });

        TextView userWins = findViewById(R.id.userWinsStat);
        TextView userLosses = findViewById(R.id.userLossesStat);
        CircleImageView highestWinsPlayerImage = findViewById(R.id.playerWithMostWinsImage);
        TextView highestWinsPlayerName = findViewById(R.id.playerWithMostWinsName);
        TextView gamesPlayed = findViewById(R.id.gamesPlayedStat);
        TextView userPPG = findViewById(R.id.userPPGStat);

        String wins = "Wins\n" + user.getWins();
        userWins.setText(wins);

        String losses = "Losses\n" + user.getLosses();
        userLosses.setText(losses);

        String games = "Games\n" + manager.getNumGamesPlayed();
        gamesPlayed.setText(games);

        if(user.getGamesPlayed() > 0) {
            int PPG = user.getPointsScored() / user.getGamesPlayed();
            String ppgString = "PPG\n" + PPG;
            userPPG.setText(ppgString);
        }

        int highestWins = 0;
        int highestWinsIndex = -1;
        for(int i = 0; i < manager.getPlayers().size(); i++){
            if(manager.getPlayers().get(i).getWins() > highestWins) {
                highestWins = manager.getPlayers().get(i).getWins();
                highestWinsIndex = i;
            }
        }

        if(highestWinsIndex != -1) {
            highestWinsPlayerImage.setImageDrawable(manager.getPlayers().get(highestWinsIndex).getPlayerProfileImage(this));
            highestWinsPlayerName.setText(manager.getPlayers().get(highestWinsIndex).getName());
        }
        else
            highestWinsPlayerImage.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        int delay = 25;

        for(int i = 0; i < statsPageContent.getChildCount(); i++) {
            AnimationController.playAnimation(StatsActivity.this, statsPageContent.getChildAt(i), R.anim.quick_zoom, delay);
            delay += 55;
        }
    }

    private void openHomePage(){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void openSettingsPage(){
        Intent settingsPage = new Intent(this, SettingsActivity.class);
        startActivity(settingsPage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

}
