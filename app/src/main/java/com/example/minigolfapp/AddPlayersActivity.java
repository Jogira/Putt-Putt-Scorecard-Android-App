package com.example.minigolfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddPlayersActivity extends AppCompatActivity
{
        private Button backX;
        private Button createGame;
        private CircleImageView player1;
        private ImageButton player2;
        private ImageButton player3;
        private ImageButton player4;
        private ImageButton player5;
        private ImageButton additionalPlayers;
        private ImageButton home;
        private ImageButton statsPage;
        private CircleImageView settingsPage;
        private boolean flipped = false;
        private Game thisGame;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);

                backX = findViewById(R.id.backX);
                createGame = findViewById(R.id.createGameButton);
                player1 = findViewById(R.id.player1Slot);
                additionalPlayers = findViewById(R.id.newPlayerButton);
                home = findViewById(R.id.homePageButton);
                statsPage = findViewById(R.id.statsPageButton);
                settingsPage = findViewById(R.id.settingsPageButton);

                backX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                goBackPage();
                        }
                });


                createGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                openPointsPage();
                        }
                });

                player1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                flipPlayerIcon();
                        }
                });

                home.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                goBackPage();
                        }
                });

                statsPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                toStatsPage();
                        }
                });

                settingsPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                toSettingsPage();
                        }
                });

        }

        private void goBackPage(){
                Intent homePage = new Intent(this, MainActivity.class);
                startActivity(homePage);
                this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
        }

        private void toStatsPage(){
                Intent statsScreen = new Intent(this, StatsActivity.class);
                startActivity(statsScreen);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        private void toSettingsPage(){
                Intent settingsScreen = new Intent(this, SettingsActivity.class);
                startActivity(settingsScreen);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        private void openPointsPage(){
                Intent addPointsPage = new Intent(this, AddPointsActivity.class);
                startActivity(addPointsPage);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        //create and add profile views dynamically from saved profiles
        private void populateProfileView(){
                for(Player p : Player.players){

                }
        }


        private void flipPlayerIcon(){
                if (!flipped) {
                        player1.setImageResource(R.drawable.ic_checked_profile);
                        flipped = true;
                }
                else {
                        player1.setImageResource(R.drawable.sean_kingston_profile);
                        flipped = false;
                }
        }
}