package com.example.minigolfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScoreCardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreCardActivity";
    int numHoles = 18;
    int currentHole = 1;
    private String filename;
    String[] playerNames = {"Sean", "Sage"};
    private boolean inEditMode = false;
    private LinearLayout scorecard;
    private TextView holeNumberView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorecard_activity);
        Intent intent = getIntent();
        filename = intent.getStringExtra("fileName");
        final Button scoreCardEditButton = findViewById(R.id.scorecardEditButton);
        final Button doneButton = findViewById(R.id.exitScorecardButton);
        scorecard = findViewById(R.id.scorecardPlayerView);
        holeNumberView = findViewById(R.id.holeTitleTextView);
        SeekBar holeSeekBar = findViewById(R.id.holeSeekBar);
        holeSeekBar.setMax(numHoles);

        holeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i < numHoles)
                    holeNumberView.setText("Hole " + (i + 1));
                else
                    holeNumberView.setText("Total");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentHole = seekBar.getProgress() + 1;
                updateScoreCard();
            }
        });

        scoreCardEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inEditMode) {
                    inEditMode = false;
                    scoreCardEditButton.setText("Edit");
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    inEditMode = true;
                    scoreCardEditButton.setText("Editing");
                    doneButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        populateScoreCardView();
    }

    public void updateScoreCard() {

        //seekbar calls this method when its value is changed.
        //this method should get current hole value, and pull score info from csv file (or preferably the game object) and update it
        //below is an example on how to change the scores

        for (int i = 0; i < scorecard.getChildCount(); i++) {
            TextView score = scorecard.getChildAt(i).findViewById(R.id.scorecardRowPlayerScore);

            //here, you will fetch scores from csv and update appropriately
//            score.setText("1");
            Log.d(TAG, "numbersent: " + i);
            String str = setscore(i);
            score.setText(str);
        }
    }

    private String setscore(int i) {
        String all = "";

        try {
            String lines = "";
            FileInputStream fileInputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
            StringBuffer stringBuffer = new StringBuffer();
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines).append("\n");
            }
            Log.d(TAG, "setScore:" + stringBuffer.toString());
            inputStreamReader.close();
            all = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] finder = all.split("\n"); //parses the csv and grabs the number hole it is looking for
        if (i > finder.length){
            return "N/A";
        }
        String temp = finder[i];
        String[] stuff = temp.split(",");
        Log.d(TAG, "number Being inputted:" + i);
        Log.d(TAG, "The line there:" + finder[i]);
        temp = stuff[1];
        Log.d(TAG, "The val that is being returned:" + temp);
        return temp;
    }

    //initial population of scorecard
    public void populateScoreCardView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 25);

        for (int i = 0; i < playerNames.length; i++) {
            View examplePlayerRow = View.inflate(this, R.layout.scorecard_row, null);
            CircleImageView playerProfile = examplePlayerRow.findViewById(R.id.scorecardRowPlayerImageView);
            Drawable b = getDrawable(R.drawable.ic_person);
            playerProfile.setImageDrawable(b);
            TextView playerName = examplePlayerRow.findViewById(R.id.scorecardRowPlayerName);
            TextView playerScore = examplePlayerRow.findViewById(R.id.scorecardRowPlayerScore);

            if (i == 0)
                params.setMargins(0, 20, 0, 25);

            playerName.setText("  " + playerNames[i]);
            playerScore.setText("N/A");
            scorecard.addView(examplePlayerRow, params);
        }
    }
}