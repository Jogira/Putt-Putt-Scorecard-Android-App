package com.minigolf.puttpoints;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class AddPlayersActivity extends AppCompatActivity {

        private AlertDialog name;
        private static final String TAG = "AddPlayersActivity";
        private String mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        private GridLayout playerSelectionContentView;
        private ArrayList<Boolean> flipped = new ArrayList<>();
        private ArrayList<Player> players;
        private int numPlayers = 0;
        private Drawable playerImage;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);
                Game.currentGame = null;
                //Button backX = findViewById(R.id.backX);
                Button createGame = findViewById(R.id.createGameButton);
                ImageButton home = findViewById(R.id.homePageButton);
                ImageButton statsPage = findViewById(R.id.statsPageButton);
                CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
                playerSelectionContentView = findViewById(R.id.playerSelectionContentView);
                players = new ArrayList<>();

//                backX.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                                goBackPage();
//                        }
//                });

                createGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                AnimationController.buttonPressSubtle(AddPlayersActivity.this, view);
                                if(numPlayers < 1) {
                                        Context context = getApplicationContext();
                                        CharSequence text = "Must select at least one player to create game.";
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

        private String createNewFile() {
                String directory = "/scores";
                String timestamped = mTimeStamp;

                StringBuilder header = new StringBuilder();
                header.append("Hole,Sean\n");

//                for(int i = 0; i < Game.currentGame.getPlayers().size(); i++){
//                        header.append(Game.currentGame.getPlayers().get(i));
//                        if(i < Game.currentGame.getPlayers().size()-1)
//                                header.append(",");
//                }

                Log.d(TAG, "Please lemme know what is happening here?");
                try {
                        FileOutputStream out = openFileOutput("score"+ timestamped + ".csv", Context.MODE_PRIVATE);
                        out.write(header.toString().getBytes());
                        out.close();
//            CSVWriter writer = new CSVWriter(new FileWriter("score" + timestamped + ".csv"));
//            List<String[]> h = new ArrayList<>();
//            String[] header = new String[]{"hole", "Sean"};
//            h.add(header);
//            Log.d(TAG, "Please lemme know what is happening here?");
//            writer.writeAll(h);
//            writer.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return ("score" + timestamped + ".csv");
        }


        private void goBackPage() {
                Intent homePage = new Intent(this, MainActivity.class);
                startActivity(homePage);
                this.overridePendingTransition(R.anim.new_page_no_anim, R.anim.slide_down);
        }


        public void createNewName() {
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
                                        row.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                        CircleImageView v = (CircleImageView) view;
                                                        AnimationController.buttonPressSubtle(AddPlayersActivity.this, view);
                                                        avatarPreview.setImageDrawable(v.getDrawable());
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

                                playerImage = avatarPreview.getDrawable();
                                Player newestPlayer = new Player(playerName, playerImage);
                                Player.players.add(newestPlayer);
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
                Game.currentGame = new Game(players, 18, "score" + mTimeStamp + ".csv");
                UserPreferencesManager manager = new UserPreferencesManager(this);
                Game.currentGame.setParsActive(manager.parsOn());

                createNewFile();
                startActivity(addPointsPage);
                this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        }

        //create and add profile views dynamically from saved profiles
        //currently creates views from DEMO profiles made in MainActivity
        private void populateProfileView() {
                playerSelectionContentView.removeAllViews();
                playerSelectionContentView.setColumnCount(3);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(55, 0, 55, 45);
                int index = 0;

                for (Player p : Player.players) {
                        View exampleProfile = View.inflate(this, R.layout.player_profile_view, null);
                        TextView nameView = exampleProfile.findViewById(R.id.playerNameView);
                        final CircleImageView profilePictureView = exampleProfile.findViewById(R.id.playerImageView);
                        nameView.setText(p.getName());

                        if(p.getName().length() > 6)
                                nameView.setText(p.getName().substring(0, 6) + "...");

                        if(flipped.size() > index && flipped.get(index))
                                profilePictureView.setImageDrawable(getDrawable(R.drawable.ic_checked_profile));
                        else
                                profilePictureView.setImageDrawable(p.getPlayerProfileImage());

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
                                createNewName();

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
                        players.add(Player.players.get(buttonIndex));
                        numPlayers++;

                } else {
                        v.setImageDrawable(Player.players.get(buttonIndex).getPlayerProfileImage());
                        flipped.set(buttonIndex, false);
                        players.remove(Player.players.get(buttonIndex));
                        numPlayers--;
                }
        }
}