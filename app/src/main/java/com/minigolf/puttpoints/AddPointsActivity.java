package com.minigolf.puttpoints;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;


public class AddPointsActivity extends AppCompatActivity {

    private EditText scoreToAdd;
    private TextView currentHoleTextView;
    private int currentPlayerTurn;
    private TextView currentPlayerName;
    private TextView parText;
    private LinearLayout playerIconView;
    private boolean parsOn;
    private float dp;
    private Button editParButton;
    private int currentPar = 2;
    private Button addScore;
    private TextView scoreNotification;
    private int previousScore = Integer.MIN_VALUE;
    private int newScore = Integer.MIN_VALUE;
    private UserPreferencesManager manager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

        manager = new UserPreferencesManager(this);
        parsOn = manager.parsOn();

        currentHoleTextView = findViewById(R.id.CurrentHole);
        LinearLayout parView = findViewById(R.id.parView);
        ImageButton increment = findViewById(R.id.incrementButton);
        ImageButton decrement = findViewById(R.id.decrementButton);
        ImageButton home = findViewById(R.id.homePageButton);
        ImageButton backOneHole = findViewById(R.id.previousHole);
        ImageButton forwardOneHole = findViewById(R.id.nextHole);
        ImageButton statsPage = findViewById(R.id.statsPageButton);
        Button pauseGame = findViewById(R.id.puaseGameButton);
        scoreNotification = findViewById(R.id.scoreNotificationView);

        addScore = findViewById(R.id.addScoreButton);
        scoreToAdd = findViewById(R.id.typeToAdd);
        Button openCard = findViewById(R.id.viewCard);
        Button endGame = findViewById(R.id.endGame);
        CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
        playerIconView = findViewById(R.id.playerIconView);
        currentPlayerName = findViewById(R.id.gameViewScoreTitle);
        editParButton = findViewById(R.id.editParButton);
        parText = findViewById(R.id.parText);

        Context context = getApplicationContext();
        CharSequence text = "Tap the score to edit with the keyboard!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if(parsOn) {
            parView.setVisibility(View.VISIBLE);
            parText.setText("Par " + currentPar);
        }
        else {
            scoreToAdd.setText("1");
            parView.setVisibility(View.GONE);
        }

        currentPlayerTurn = Game.currentGame.currentPlayerTurn;
        currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
        currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s Score");
        currentPlayerTurn = Game.currentGame.currentPlayerTurn;

        editParButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openParPopup();
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementScore();
                AnimationController.buttonPress(AddPointsActivity.this, view);
            }
        });

        scoreToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(AddPointsActivity.this, view);
            }
        });


        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreToAdd.clearFocus();
                decrementScore();
                AnimationController.buttonPress(AddPointsActivity.this, view);
            }
        });

        backOneHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPress(AddPointsActivity.this, view);
                scoreToAdd.clearFocus();
                if (Game.currentGame.getCurrentHole() > 1) {
                    Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole() - 1);
                    currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
                    currentPlayerTurn = 0;
                    updatePlayerTurn(currentPlayerTurn);
                    updatePlayerProfileCheckMarks();
                }
            }
        });

        forwardOneHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreToAdd.clearFocus();
                AnimationController.buttonPress(AddPointsActivity.this, view);
                if (Game.currentGame.getCurrentHole() < 18)
                    incrementHole();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHomeScreen();
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

        addScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreToAdd.clearFocus();
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                recordPlayerScore();
                incrementPlayerTurn();
            }
        });

        openCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                openScorecard(false);
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                openWinnerScreen();
            }
        });

        pauseGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);
                manager.addGame(Game.currentGame);
                toHomeScreen();
            }
        });

        dp = AddPointsActivity.this.getResources().getDimension(R.dimen.pixelsToDP);
        populatePlayerIconView();
        updateAddScoreButton();
    }



    private void recordPlayerScore(){
        int score = Integer.parseInt(scoreToAdd.getText().toString());

        if(parsOn) {
            Game.currentGame.setParAtHole(Game.currentGame.getCurrentHole()-1, currentPar);
            score -= currentPar;

            currentPar = 2;
            parText.setText("Par " + currentPar);
        }

        Game.currentGame.setPlayerScore(currentPlayerTurn, score);
    }


    public void incrementPlayerTurn() {
        int numPlayers = Game.currentGame.getPlayers().size();

        if(currentPlayerTurn < numPlayers - 1)
            currentPlayerTurn++;
        else
            incrementHole();

        updateAddScoreButton();

        updatePlayerTurn(currentPlayerTurn);
    }


    public void updateAddScoreButton(){
        int numPlayers = Game.currentGame.getPlayers().size();

        if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles() && currentPlayerTurn == numPlayers - 1)
            addScore.setText("Finish game!");
        else if(currentPlayerTurn == numPlayers -1)
            addScore.setText("Next Hole");
        else
            addScore.setText("Next Player");
    }


    public void incrementHole() {
        if(holeFinished()) {

            if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
                Game.currentGame.setActive(false);
                openWinnerScreen();
            }

            else {
                Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole() + 1);
                currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
                currentPlayerTurn = 0;
                updatePlayerTurn(currentPlayerTurn);
                updatePlayerProfileCheckMarks();
            }
        }
    }


    private void updatePlayerProfileCheckMarks(){
        for(int i = 0; i < Game.currentGame.getPlayers().size(); i++) {
            View player = playerIconView.getChildAt(i);
            ImageView playerCheck = player.findViewById(R.id.check);

            if(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[i] != Integer.MIN_VALUE)
                playerCheck.setVisibility(View.VISIBLE);
            else
                playerCheck.setVisibility(View.INVISIBLE);
        }
    }


    public boolean holeFinished(){
        boolean finished = true;
        int[] currentHole = Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole() - 1);
        for(int x : currentHole){
            if(x == Integer.MIN_VALUE){
                finished = false;
                break;
            }
        }

        return finished;
    }


    //initial population of the player profile views in the top of the screen
    private void populatePlayerIconView(){
        playerIconView.removeAllViews();

        for(int i = 0; i < Game.currentGame.getPlayers().size(); i++) {

            LinearLayoutCompat playerProfileView = (LinearLayoutCompat) View.inflate(this, R.layout.player_profile_view_game, null);
            playerProfileView.setTag(i);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            params.setMarginEnd((int)(8*dp));
            params.setMarginStart((int) (8*dp));
            params.setMargins((int) (8*dp), 0, (int) (8*dp), 0);
            params.gravity = Gravity.CENTER_VERTICAL;

            CircleImageView playerImageView = playerProfileView.findViewById(R.id.playerImageView);
            playerImageView.setImageDrawable(Game.currentGame.getPlayers().get(i).getPlayerProfileImage(this));
            LinearLayoutCompat.LayoutParams imageParams = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            playerImageView.setLayoutParams(imageParams);
            imageParams.height = (int) dp * 58;
            imageParams.width = (int) dp * 58;

            ImageView checkImage = playerProfileView.findViewById(R.id.check);
            if(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[i] == Integer.MIN_VALUE)
                checkImage.setVisibility(View.INVISIBLE);
            else
                checkImage.setVisibility(View.VISIBLE);

            if(i == Game.currentGame.currentPlayerTurn) {
                playerImageView.setBorderWidth((int) (3 * dp));
                imageParams.height = (int) dp * 65;
                imageParams.width = (int) dp * 65;
            }

            playerProfileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    recordPlayerScore();
                    updatePlayerTurn((Integer) v.getTag());
                }
            });
            playerIconView.addView(playerProfileView, params);
        }
    }


    //updates view of players, also updates currentPlayerTurn int and Game.currentPlayerTurn
    @SuppressLint("SetTextI18n")
    private void updatePlayerTurn(int index) {
        updatePlayerProfileCheckMarks();

        for(int i = 0; i < playerIconView.getChildCount(); i++) {
            View player = playerIconView.getChildAt(i);
            CircleImageView playerProfile = player.findViewById(R.id.playerImageView);

            if(i == index) {
                    playerProfile.setBorderWidth((int) (3*dp));
                    currentPlayerTurn = i;
                    Game.currentGame.currentPlayerTurn = currentPlayerTurn;
                    currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s Score");

                    if(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[currentPlayerTurn] != Integer.MIN_VALUE)
                        scoreToAdd.setText(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[currentPlayerTurn]+"");
                    else {
                        if(parsOn)
                            scoreToAdd.setText("0");
                        else
                            scoreToAdd.setText("1");
                    }

                    updateAddScoreButton();
                    AnimationController.playAnimation(this, playerProfile, R.anim.scale_up);
                    playerProfile.getLayoutParams().height = (int) dp * 65;
                    playerProfile.getLayoutParams().width = (int) dp * 65;
            }
            else {
                playerProfile.setBorderWidth(0);
                AnimationController.playAnimation(this, playerProfile, R.anim.scale_down);
                playerProfile.getLayoutParams().height = (int) dp * 58;
                playerProfile.getLayoutParams().width = (int) dp * 58;
            }
            player.requestLayout();
        }
    }


    private void incrementScore() {
        scoreToAdd.clearFocus();
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        if((score + 1) <= 99)
            score++;
        String incrementedScore = valueOf(score);
        scoreToAdd.setText(incrementedScore);
    }


    private void decrementScore() {
        scoreToAdd.clearFocus();
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        if((score - 1) >= 1 || parsOn)
            score--;
        String decrementedScore = valueOf(score);
        scoreToAdd.setText(decrementedScore);
    }


    private void openScorecard(boolean gameFinished) {
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        scorecard.putExtra("gameFinished", gameFinished);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
    }

    private void openWinnerScreen(){
        Intent winnerScreen = new Intent(this, WinnerScreenActivity.class);
        startActivity(winnerScreen);
        this.overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
    }

    private void openParPopup() {
        AnimationController.buttonPressSubtle(this, editParButton);
        LinearLayout viewGroup =  this
                .findViewById(R.id.parPopupView);

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.popup_par_edit, viewGroup);
        final PopupWindow popup = new PopupWindow(this);

        popup.setContentView(layout);
        popup.setWidth((int) (dp*175));
        popup.setHeight((int) (dp*150));
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));

        popup.showAsDropDown(editParButton, -((popup.getWidth()/3)), 10);
        Button closePopup = layout.findViewById(R.id.parPopupDoneButton);
        final ImageButton decrementPar = layout.findViewById(R.id.decrementPar);
        final ImageButton incrementPar = layout.findViewById(R.id.incrementPar);
        final TextView parNum = layout.findViewById(R.id.parPopupParNumber);

        decrementPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, decrementPar);
                if(Integer.parseInt((String) parNum.getText()) != 0) {
                    currentPar--;
                    parNum.setText(currentPar + "");
                }
            }
        });

        incrementPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, incrementPar);
                currentPar++;
                parNum.setText(currentPar + "");
            }
        });

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                parText.setText("Par " + currentPar);
            }
        });

    }


    private void toHomeScreen() {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    private void toSettingsPage() {
        Intent settingsScreen = new Intent(this, SettingsActivity.class);
        startActivity(settingsScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    private void toStatsPage() {
        Intent statsScreen = new Intent(this, StatsActivity.class);
        startActivity(statsScreen);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

}