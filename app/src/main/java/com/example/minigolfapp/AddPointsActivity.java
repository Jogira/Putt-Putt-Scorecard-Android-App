package com.example.minigolfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AddPointsActivity extends AppCompatActivity
{
    private ImageButton increment;
    private ImageButton decrement;
    private TextView scoreToAdd;
    private Button openCard;
    private Button endGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

        increment = findViewById(R.id.incrementButton);
        decrement = findViewById(R.id.decrementButton);
        scoreToAdd = findViewById(R.id.scoreToAdd);
        openCard = findViewById(R.id.viewCard);
        endGame = findViewById(R.id.endGame);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementScore();
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementScore();
            }
        });

        openCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScorecard();
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScorecard();
            }
        });



    }


    private void incrementScore()
    {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score++;
        String incrementedScore = String.valueOf(score);
        scoreToAdd.setText(incrementedScore);
    }

    private void decrementScore()
    {
        int score = Integer.parseInt(scoreToAdd.getText().toString().trim());
        score--;
        String decrementedScore = String.valueOf(score);
        scoreToAdd.setText(decrementedScore);
    }

    private void openScorecard(){
        Intent scorecard = new Intent(this, ScoreCardActivity.class);
        startActivity(scorecard);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}