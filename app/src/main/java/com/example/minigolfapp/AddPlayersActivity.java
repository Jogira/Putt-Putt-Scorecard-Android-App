package com.example.minigolfapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.opencsv.CSVWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.CENTER;


public class AddPlayersActivity extends AppCompatActivity {
        private AlertDialog name;
        private EditText playerName;
        private static final String TAG = "AddPlayersActivity";
        private String mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        private GridLayout playerSelectionContentView;
        private final ArrayList<Boolean> flipped = new ArrayList<>();
        private ArrayList<Player> players;
        private int numPlayers = 0;
        private ImageButton newPlayer;
        private Player newestPlayer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);
                Game.currentGame = null;
                //Button backX = findViewById(R.id.backX);
                Button createGame = findViewById(R.id.createGameButton);
                //additionalPlayers = findViewById(R.id.newPlayerButton);
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
                final View namePopupView = getLayoutInflater().inflate(R.layout.popup, null);
                playerName = namePopupView.findViewById(R.id.enterNamePopup);
                Button cancel = namePopupView.findViewById(R.id.cancel_button);
                Button confirm = namePopupView.findViewById(R.id.confirm_button);
                nameBuilder.setView(namePopupView);
                //name.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                name = nameBuilder.create();
                name.show();

                confirm.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                                EditText confirmedName = namePopupView.findViewById(R.id.enterNamePopup);
                                String playerName = confirmedName.getText().toString();
                                List<String> colors = new ArrayList<>();
                                colors.add("#e84e40"); //red
                                colors.add("#ec407a"); //pink
                                colors.add("#ab47bc"); //purple
                                colors.add("#7e57c2"); //deep purple
                                colors.add("#5c6bc0"); //indigo
                                colors.add("#26a69a"); //teal
                                colors.add("#2baf2b"); //green
                                colors.add("#fc9c6b"); //sunset orange
                                colors.add("#00f444"); //lime green
                                colors.add("#fbff00"); //rubber duck yellow
                                colors.add("#ffca28"); //amber
                                colors.add("#53C2C3"); //sky blue
                                colors.add("#DAAE19"); //gold
                                colors.add("#A9D47C"); //dead grass
                                colors.add("#D53046"); //rose red
                                colors.add("#007eff"); //deep blue
                                colors.add("#008c99"); //baja blast

                                Random n= new Random();
                                int color = n.nextInt(colors.size());
                                Drawable profileImageNew = getDrawable(R.drawable.ic_person);
                                profileImageNew.setColorFilter(Color.parseColor(colors.get(color)), PorterDuff.Mode.MULTIPLY);
                                newestPlayer = new Player(playerName, profileImageNew);
                                Player.players.add(newestPlayer);
                                populateProfileView();
                                colors.clear();
//                              Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//                              toast.show();
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

                                if(p.getName().length() > 8)
                                        nameView.setText(p.getName().substring(0, 8) + "...");

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