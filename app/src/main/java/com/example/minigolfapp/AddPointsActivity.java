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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);

        increment = findViewById(R.id.incrementButton);
        decrement = findViewById(R.id.decrementButton);
        scoreToAdd = findViewById(R.id.scoreToAdd);

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
}