package com.example.minigolfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class AddPlayersActivity extends AppCompatActivity
{
        private Button backX;
        private Button createGame;
        private ImageButton player1;
        private ImageButton player2;
        private ImageButton player3;
        private ImageButton player4;
        private ImageButton player5;
        private ImageButton additionalPlayers;
        private boolean flipped = false;
        private Game thisGame;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);

                backX = findViewById(R.id.backX);
                createGame = findViewById(R.id.createGame);
                player1 = findViewById(R.id.player1Slot);
                additionalPlayers = findViewById(R.id.additionalPlayer);

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
        }

        private void goBackPage(){
                Intent homePage = new Intent(this, MainActivity.class);
                startActivity(homePage);
                this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
        }

        private void openPointsPage(){
                Intent addPointsPage = new Intent(this, AddPointsActivity.class);
                startActivity(addPointsPage);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        private void populateProfileView(){
                for(Player p : Player.players){

                }
        }


        private void flipPlayerIcon(){
                if (!flipped)
                {
                        player1.setBackgroundResource(R.drawable.rounded_background);
                        flipped = true;
                }
                else
                {
                        player1.setBackgroundResource(R.drawable.sean_kingston_profile);
                        flipped = false;
                }
        }
}