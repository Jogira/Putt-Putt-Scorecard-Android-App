package com.minigolf.puttpoints;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;


public class AddPointsActivity extends AppCompatActivity {

    private TextView scoreToAdd;
    private TextView currentHoleTextView;
    private final String fileName = Game.currentGame.getFileName();
    private int currentPlayerTurn = Game.currentGame.currentPlayerTurn;
    private TextView currentPlayerName;
    private TextView parText;
    private LinearLayout playerIconView;
    private boolean parsOn;
    private float dp;
    private Button editParButton;
    private int currentPar = 2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

        UserPreferencesManager manager = new UserPreferencesManager(this);
        parsOn = manager.parsOn();

        currentHoleTextView = findViewById(R.id.CurrentHole);
        ImageButton increment = findViewById(R.id.incrementButton);
        ImageButton decrement = findViewById(R.id.decrementButton);
        ImageButton home = findViewById(R.id.homePageButton);
        ImageButton statsPage = findViewById(R.id.statsPageButton);
        Button addScore = findViewById(R.id.addScoreButton);
        scoreToAdd = findViewById(R.id.scoreToAdd);
        Button openCard = findViewById(R.id.viewCard);
        Button endGame = findViewById(R.id.endGame);
        CircleImageView settingsPage = findViewById(R.id.settingsPageButton);
        playerIconView = findViewById(R.id.playerIconView);
        currentPlayerName = findViewById(R.id.gameViewScoreTitle);
        editParButton = findViewById(R.id.editParButton);
        parText = findViewById(R.id.parText);

        if(parsOn) {
            editParButton.setVisibility(View.VISIBLE);
            parText.setText("Par " + currentPar);
        }
        else
            editParButton.setVisibility(View.GONE);

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

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementScore();
                AnimationController.buttonPress(AddPointsActivity.this, view);
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
            private static final String TAG = "AddPointsActivity";

            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(AddPointsActivity.this, view);

                int score = Integer.parseInt((String) scoreToAdd.getText());

                if(parsOn) {
                    Game.currentGame.setParAtHole(Game.currentGame.getCurrentHole()-1, currentPar);
                    score -= currentPar;

                    currentPar = 2;
                    parText.setText("Par " + currentPar);
                }

                Game.currentGame.setPlayerScore(currentPlayerTurn, score);

//                String lines = "";
//                StringBuilder newStr = new StringBuilder();
//
//                Log.d(TAG, "Input: " + Game.currentGame.getCurrentHole());
//                newStr.append(Game.currentGame.getCurrentHole()).append(",").append(scoreToAdd.getText()).append("\n");
//                Log.d(TAG, "Output:" + newStr);
//
//                try {
//
//                    FileOutputStream out = openFileOutput(fileName, Context.MODE_APPEND);
//                    out.write(newStr.toString().getBytes());
//                    out.close();
//
//                    FileInputStream fileInputStream = openFileInput(fileName);
//                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//
//                    BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
//                    StringBuffer stringBuffer = new StringBuffer();
//
//                    while ((lines = bufferedReader.readLine()) != null)
//                        stringBuffer.append(lines).append("\n");
//
//                    //Log.d(TAG, "Reach here:" + stringBuffer.toString());
//                    inputStreamReader.close();
//                }
//                catch (IOException e) { e.printStackTrace(); }

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
                openScorecard(true);
                //openWinnerScreen();
            }
        });

        dp = AddPointsActivity.this.getResources().getDimension(R.dimen.pixelsToDP);
        populatePlayerIconView();
    }


    public void incrementPlayerTurn() {
        int numPlayers = Game.currentGame.getPlayers().size();

        if(holeFinished()){
            if(Game.currentGame.getCurrentHole() == Game.currentGame.getNumHoles()) {
                Game.currentGame.setActive(false);
                openScorecard(true);
                //openWinnerScreen();
            }
            else {
                Game.currentGame.setCurrentHole(Game.currentGame.getCurrentHole() + 1);
                currentHoleTextView.setText(String.valueOf(Game.currentGame.getCurrentHole()));
                currentPlayerTurn = 0;
            }
        }
        else if(currentPlayerTurn < numPlayers - 1)
            currentPlayerTurn++;

        else {
            Context context = getApplicationContext();
            CharSequence text = "A player is missing a score!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            currentPlayerTurn = 0;
        }

        updatePlayerTurn(currentPlayerTurn);
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

            final CircleImageView playerImageView = new CircleImageView(this);
            playerImageView.setImageDrawable(Game.currentGame.getPlayers().get(i).getPlayerProfileImage());
            playerImageView.setTag(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

            params.width = (int) dp * 60;
            params.height = (int) dp * 60;
            params.setMarginStart(20);
            params.setMarginEnd(20);
            params.gravity = Gravity.CENTER_VERTICAL;
            playerImageView.setBorderColor(Color.parseColor("#1CD371"));
            playerImageView.setBorderWidth(0);

            if(i == Game.currentGame.currentPlayerTurn) {
                playerImageView.setBorderWidth((int) (3 * dp));
                params.height = (int) dp * 67;
                params.width = (int) dp * 67;
            }

            playerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    updatePlayerTurn((Integer) v.getTag());
                }
            });
            playerIconView.addView(playerImageView, params);
        }
    }


    //updates view of players in top, also updates currentPlayerTurn int and Game.currentPlayerTurn
    @SuppressLint("SetTextI18n")
    private void updatePlayerTurn(int index) {

        for(int i = 0; i < playerIconView.getChildCount(); i++) {
            CircleImageView playerProfile = (CircleImageView) playerIconView.getChildAt(i);
            if(i == index) {
                    playerProfile.setBorderWidth((int) (3*dp));
                    currentPlayerTurn = i;
                    Game.currentGame.currentPlayerTurn = currentPlayerTurn;
                    currentPlayerName.setText(Game.currentGame.getPlayers().get(currentPlayerTurn).getName() + "'s Score");

                    if(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[currentPlayerTurn] != Integer.MIN_VALUE)
                        scoreToAdd.setText(Game.currentGame.getPlayerScores().get(Game.currentGame.getCurrentHole()-1)[currentPlayerTurn]+"");
                    else
                        scoreToAdd.setText("0");

                    AnimationController.playAnimation(this, playerProfile, R.anim.scale_up);
                    playerProfile.getLayoutParams().height = (int) dp * 67;
                    playerProfile.getLayoutParams().width = (int) dp * 67;
            }
            else {
                playerProfile.setBorderWidth(0);
                AnimationController.playAnimation(this, playerProfile, R.anim.scale_down);
                playerProfile.getLayoutParams().height = (int) dp * 60;
                playerProfile.getLayoutParams().width = (int) dp * 60;
            }
            playerProfile.requestLayout();
        }
    }


    private void incrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score++;
        String incrementedScore = valueOf(score);
        scoreToAdd.setText(incrementedScore);
    }


    private void decrementScore() {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        if((score - 1) >= 0 || parsOn)
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
        Intent winnerScreen = new Intent(this, WinnerScreen.class);
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