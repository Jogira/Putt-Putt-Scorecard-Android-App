package com.example.minigolfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddPlayersActivity extends AppCompatActivity {

        private GridLayout playerSelectionContentView;
        private ArrayList<Boolean> flipped = new ArrayList<>();
        private Game thisGame;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);

                Button backX = findViewById(R.id.backX);
                Button createGame = findViewById(R.id.createGameButton);
              //  player1 = findViewById(R.id.player1Slot);
                ImageButton additionalPlayers = findViewById(R.id.newPlayerButton);
                ImageButton home = findViewById(R.id.homePageButton);
                ImageButton statsPage = findViewById(R.id.statsPageButton);
                CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
                playerSelectionContentView = findViewById(R.id.playerSelectionContentView);

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

//                player1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                                flipPlayerIcon();
//                        }
//                });

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
                populateProfileView();
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
        //currently creates views from DEMO profiles made in MainActivity
        private void populateProfileView(){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(75, 0, 75, 45);
                playerSelectionContentView.setColumnCount(3);
                int index = 0;

                for(Player p : Player.players){
                        View exampleProfile = View.inflate(this, R.layout.player_profile_view, null);
                        TextView nameView = exampleProfile.findViewById(R.id.playerNameView);
                        final CircleImageView profilePictureView = exampleProfile.findViewById(R.id.playerImageView);
                        nameView.setText(p.getName());
                        profilePictureView.setImageDrawable(p.getPlayerProfileImage());

                        profilePictureView.setTag(index);
                        profilePictureView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        flipPlayerIcon((CircleImageView) view);
                                }
                        });
                        playerSelectionContentView.addView(exampleProfile, index, params);
                        flipped.add(false);
                        index++;
                }


        }


        private void flipPlayerIcon(CircleImageView v){
                int buttonIndex = (int)v.getTag();

                if (!flipped.get(buttonIndex)) {
                        v.setImageResource(R.drawable.ic_checked_profile);
                        flipped.set(buttonIndex, true);
                }
                else {
                        v.setImageDrawable(Player.players.get(buttonIndex).getPlayerProfileImage());
                        flipped.set(buttonIndex, false);
                }
        }
}