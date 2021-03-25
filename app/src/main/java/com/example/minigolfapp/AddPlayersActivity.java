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
        private AlertDialog.Builder nameBuilder;
        private AlertDialog name;
        private EditText playerName;
        private Button confirm, cancel;
        private static final String TAG = "AddPlayersActivity";
        private String mTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        private GridLayout playerSelectionContentView;
        private ArrayList<Boolean> flipped = new ArrayList<>();
        private ArrayList<Player> players;
        private int numPlayers = 0;
        private ImageButton newPlayer;
        private boolean addingNewPlayer = false;
        private Player newestPlayer;
        private int indexNewPlayer = 4;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.add_players);
                Game.currentGame = null;
                //Button backX = findViewById(R.id.backX);
                Button createGame = findViewById(R.id.createGameButton);
                ImageButton additionalPlayers = findViewById(R.id.newPlayerButton);
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

                additionalPlayers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                AnimationController.buttonPress(AddPlayersActivity.this, view);
                                createNewName();

                        }
                });


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

        public void createNewName()
        {
                nameBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
                final View namePopupView = getLayoutInflater().inflate(R.layout.popup, null);
                playerName = (EditText) namePopupView.findViewById(R.id.enterNamePopup);
                cancel = (Button) namePopupView.findViewById(R.id.cancel_button);
                confirm = (Button) namePopupView.findViewById(R.id.confirm_button);
                nameBuilder.setView(namePopupView);
                //name.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                name = nameBuilder.create();
                name.show();

                confirm.setOnClickListener(new View.OnClickListener(){

                                                         @Override
                                                         public void onClick(View view) {
                                                                 EditText confirmedName = (EditText) namePopupView.findViewById(R.id.enterNamePopup);
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
//                                                                 CharSequence text = playerName + " was added.";
                                                                 Drawable profileImageNew = getDrawable(R.drawable.ic_person);
                                                                 profileImageNew.setColorFilter(Color.parseColor(colors.get(color)), PorterDuff.Mode.MULTIPLY);
                                                                 newestPlayer = new Player(playerName, profileImageNew);
                                                                 Player.players.add(newestPlayer);
                                                                 addingNewPlayer = true;
                                                                 populateProfileView();
                                                                 addingNewPlayer = false;
                                                                 colors.clear();
//                                                                 Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//                                                                 toast.show();
                                                                 name.dismiss();
                                                         }
                                                 }
                );

                cancel.setOnClickListener(new View.OnClickListener(){
                                                   @Override
                                                   public void onClick(View view) {
                                                           name.dismiss();
                                                   }
                                           }
                );
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(75, 0, 75, 45);
                playerSelectionContentView.setColumnCount(3);
                int index = 0;

                if (!addingNewPlayer)
                {
                        for (Player p : Player.players) {
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
                                                playAnimation(profilePictureView, R.anim.button_press_in, 0);
                                                playAnimation(profilePictureView, R.anim.button_press_out, 100);
                                        }
                                });
                                playerSelectionContentView.addView(exampleProfile, index, params);
                                flipped.add(false);
                                index++;
                        }
                }

                else
                {
                                View exampleProfile = View.inflate(this, R.layout.player_profile_view, null);
                                TextView nameView = exampleProfile.findViewById(R.id.playerNameView);
                                final CircleImageView profilePictureView = exampleProfile.findViewById(R.id.playerImageView);
                                nameView.setText(newestPlayer.getName());
                                profilePictureView.setImageDrawable(newestPlayer.getPlayerProfileImage());

                                profilePictureView.setTag(indexNewPlayer);
                                profilePictureView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                                flipPlayerIcon((CircleImageView) view);
                                                playAnimation(profilePictureView, R.anim.button_press_in, 0);
                                                playAnimation(profilePictureView, R.anim.button_press_out, 100);
                                        }
                                });
                                playerSelectionContentView.addView(exampleProfile, indexNewPlayer, params);
                                flipped.add(false);
                                indexNewPlayer++;
                }
        }

        //create and add profile views dynamically from saved profiles
        //currently creates views from DEMO profiles made in MainActivity
        private void displayNewPlayer() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(75, 0, 75, 45);
                playerSelectionContentView.setColumnCount(3);
                int index = 0;

                for (Player p : Player.players) {
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
                                        playAnimation(profilePictureView, R.anim.button_press_in, 0);
                                        playAnimation(profilePictureView, R.anim.button_press_out, 100);
                                }
                        });
                        playerSelectionContentView.addView(exampleProfile, index, params);
                        flipped.add(false);
                        index++;
                }
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