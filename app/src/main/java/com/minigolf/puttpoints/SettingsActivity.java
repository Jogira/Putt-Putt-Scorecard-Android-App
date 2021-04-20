package com.minigolf.puttpoints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private TextView parsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final UserPreferencesManager manager = new UserPreferencesManager(this);

        ImageButton statsButton = findViewById(R.id.statsPageButton);
        ImageButton homeButton = findViewById(R.id.homePageButton);
        CircleImageView settingsButton = findViewById(R.id.settingsPageButton);
        settingsButton.setImageDrawable(manager.getPlayers().get(0).getPlayerProfileImage(this));
        Switch parsToggle = findViewById(R.id.parToggle);
        parsText = findViewById(R.id.parToggleText);

        parsToggle.setChecked(manager.parsOn());
        if(manager.parsOn())
            parsText.setText("Pars On");
        else
            parsText.setText("Pars Off");

        parsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                manager.updatePars(isChecked);
                if(isChecked)
                    parsText.setText("Pars On");
                else
                    parsText.setText("Pars Off");
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatsPage();
            }
        });
    }

    private void openHomePage(){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void openStatsPage(){
        Intent statsPage = new Intent(this, StatsActivity.class);
        startActivity(statsPage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
