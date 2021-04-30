package com.minigolf.puttpoints;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddPlayersActivity extends AppCompatActivity {

        private AlertDialog name;
        private GridLayout playerSelectionContentView;
        private ArrayList<Boolean> flipped = new ArrayList<>();
        private ArrayList<Player> players;
        private int numPlayers = 0;
        private UserPreferencesManager manager;
        private int selectedAvatar = 0;
        private float dp;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);
                Game.currentGame = null;
                Button createGame = findViewById(R.id.createGameButton);
                ImageButton home = findViewById(R.id.homePageButton);
                ImageButton statsPage = findViewById(R.id.statsPageButton);
                CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
                playerSelectionContentView = findViewById(R.id.playerSelectionContentView);
                players = new ArrayList<>();
                manager = new UserPreferencesManager(this);
                settingsPage.setImageDrawable(manager.getPlayers().get(0).getPlayerProfileImage(this));
                dp = AddPlayersActivity.this.getResources().getDimension(R.dimen.pixelsToDP);

                createGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                AnimationController.buttonPressSubtle(AddPlayersActivity.this, view);
                                if(numPlayers < 1) {
                                        Context context = getApplicationContext();
                                        CharSequence text = "Must select at least one player to create a game!";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                }
                                else {
                                        openPointsPage();
                                }

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
                populateProfileView();
        }


        private void goBackPage() {
                Intent homePage = new Intent(this, MainActivity.class);
                startActivity(homePage);
                this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
        }


        public void createNewPlayer() {
                AlertDialog.Builder nameBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
                final View namePopupView = getLayoutInflater().inflate(R.layout.popup_new_player, null);
                EditText playerName = namePopupView.findViewById(R.id.newProfileNameEntry);
                final CircleImageView avatarPreview = namePopupView.findViewById(R.id.addNewProfileImageView);
                Button cancel = namePopupView.findViewById(R.id.cancel_button);
                Button confirm = namePopupView.findViewById(R.id.confirm_button);
                nameBuilder.setView(namePopupView);
                name = nameBuilder.create();
                name.show();

                TableLayout table = namePopupView.findViewById(R.id.playerAvatarImages);


                for(int i = 0; i < table.getChildCount(); i++) {
                        View view = table.getChildAt(i);
                        if (view instanceof TableRow) {
                                final TableRow row = (TableRow) view;

                                for(int j = 0; j < row.getChildCount(); j++){
                                        int index = j;
                                        if(i == 1)
                                                index += row.getChildCount();

                                        final int id = index;
                                        row.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                        CircleImageView v = (CircleImageView) view;
                                                        AnimationController.buttonPressSubtle(AddPlayersActivity.this, view);
                                                        avatarPreview.setImageDrawable(v.getDrawable());
                                                        selectedAvatar = id;
                                                }
                                        });
                                }

                        }
                }

                confirm.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                                EditText confirmedName = namePopupView.findViewById(R.id.newProfileNameEntry);

                                String playerName = confirmedName.getText().toString();

                                UserPreferencesManager manager = new UserPreferencesManager(AddPlayersActivity.this);
                                int playerID = manager.getLastPlayerID();
                                manager.updateLastPlayerID(playerID+1);
                                Player newestPlayer = new Player(playerName, selectedAvatar, playerID);

                                manager.addPlayer(newestPlayer);

                                populateProfileView();
                                name.dismiss();
                        }
                });

                cancel.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                                name.dismiss();
                        }
                });
        }

        private void toStatsPage() {
                Intent statsScreen = new Intent(this, StatsActivity.class);
                startActivity(statsScreen);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        private void toSettingsPage() {
                Intent settingsScreen = new Intent(this, SettingsActivity.class);
                startActivity(settingsScreen);
                this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        private void openPointsPage() {
                Intent addPointsPage = new Intent(this, AddPointsActivity.class);
                Game.currentGame = new Game(AddPlayersActivity.this, players, 18);
                UserPreferencesManager manager = new UserPreferencesManager(this);
                Game.currentGame.setParsActive(manager.parsOn());

                startActivity(addPointsPage);
                this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        }

        //create and add profile views dynamically from saved profiles
        //currently creates views from DEMO profiles made in MainActivity
        private void populateProfileView() {
                playerSelectionContentView.removeAllViews();
                playerSelectionContentView.setColumnCount(3);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins((int)(15*dp), 0, (int)(15*dp), (int)(15*dp));
                int index = 0;

                for (Player p : manager.getPlayers()) {
                        View exampleProfile = View.inflate(this, R.layout.player_profile_view, null);
                        TextView nameView = exampleProfile.findViewById(R.id.playerNameView);
                        final CircleImageView profilePictureView = exampleProfile.findViewById(R.id.playerImageView);
                        nameView.setText(p.getName());

                        if(p.getName().length() > 9)
                                nameView.setText(p.getName().substring(0, 9) + "...");

                        if(flipped.size() > index && flipped.get(index))
                                profilePictureView.setImageDrawable(getDrawable(R.drawable.ic_checked_profile));
                        else
                                profilePictureView.setImageDrawable(p.getPlayerProfileImage(this));

                        profilePictureView.setTag(index);

                        profilePictureView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                        flipPlayerIcon((CircleImageView) view);
                                        playAnimation(profilePictureView, R.anim.button_press_in, 0);
                                        playAnimation(profilePictureView, R.anim.button_press_out, 100);
                                }
                        });
                        playerSelectionContentView.addView(exampleProfile, index, params);
                        flipped.add(false);
                        index++;
                }

                //create and add the "new player" button
                View addPlayersView = View.inflate(this, R.layout.player_profile_view, null);
                TextView nameView = addPlayersView.findViewById(R.id.playerNameView);
                final CircleImageView addPlayerIcon = addPlayersView.findViewById(R.id.playerImageView);
                nameView.setText("New");
                addPlayerIcon.setImageDrawable(getDrawable(R.drawable.ic_add_player));
                addPlayersView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                AnimationController.buttonPress(AddPlayersActivity.this, view);
                                createNewPlayer();

                        }
                });
                playerSelectionContentView.addView(addPlayersView, index, params);
        }

        private void playAnimation(final View v, final int animationId, int delayMS) {
                if (v != null) {
                        new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        Animation animation = AnimationUtils.loadAnimation(AddPlayersActivity.this, animationId);
                                        animation.setDuration(animation.getDuration());
                                        animation.setFillAfter(true);
                                        v.startAnimation(animation);
                                }
                        }, delayMS);
                }
        }

        private void flipPlayerIcon(CircleImageView v) {
                int buttonIndex = (int) v.getTag();

                if (!flipped.get(buttonIndex)) {
                        v.setImageResource(R.drawable.ic_checked_profile);
                        flipped.set(buttonIndex, true);
                        players.add(manager.getPlayers().get(buttonIndex));
                        numPlayers++;

                } else {
                        v.setImageDrawable(manager.getPlayers().get(buttonIndex).getPlayerProfileImage(this));
                        flipped.set(buttonIndex, false);
                        players.remove(manager.getPlayers().get(buttonIndex));
                        numPlayers--;
                }
        }
}