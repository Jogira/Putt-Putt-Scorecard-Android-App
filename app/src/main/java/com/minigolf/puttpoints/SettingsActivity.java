package com.minigolf.puttpoints;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private TextView parsText;
    private AlertDialog name;
    private int selectedAvatar = 0;
    private UserPreferencesManager manager;
    private Player user;
    private Button updateProfileButton;
    private CircleImageView settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        manager = new UserPreferencesManager(this);
        user = manager.getPlayers().get(0);

        ImageButton statsButton = findViewById(R.id.statsPageButton);
        ImageButton homeButton = findViewById(R.id.homePageButton);
        settingsButton = findViewById(R.id.settingsPageButton);
        settingsButton.setImageDrawable(manager.getPlayers().get(0).getPlayerProfileImage(this));
        updateProfileButton = findViewById(R.id.editProfileButton);
        Switch parsToggle = findViewById(R.id.parToggle);
        parsText = findViewById(R.id.parToggleText);

        updateProfileButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, user.getPlayerProfileImage(this), null);

        parsToggle.setChecked(manager.parsOn());
        if(manager.parsOn())
            parsText.setText("Pars On");
        else
            parsText.setText("Pars Off");

        parsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                manager.updatePars(isChecked);
                if(isChecked)
                    parsText.setText("Pars On");
                else
                    parsText.setText("Pars Off");
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationController.buttonPressSubtle(SettingsActivity.this, view);
                updateProfile();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatsPage();
            }
        });
    }

    private void openHomePage(){
        Intent homePage = new Intent(this, MainActivity.class);
        startActivity(homePage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void openStatsPage(){
        Intent statsPage = new Intent(this, StatsActivity.class);
        startActivity(statsPage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }


    public void updateProfile() {
        AlertDialog.Builder nameBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        final View namePopupView = getLayoutInflater().inflate(R.layout.popup_new_player, null);
        EditText confirmedName = namePopupView.findViewById(R.id.newProfileNameEntry);
        confirmedName.setText(user.getName());
        final CircleImageView avatarPreview = namePopupView.findViewById(R.id.addNewProfileImageView);
        avatarPreview.setImageDrawable(user.getPlayerProfileImage(this));
        Button cancel = namePopupView.findViewById(R.id.cancel_button);
        Button confirm = namePopupView.findViewById(R.id.confirm_button);
        confirm.setText("Update");
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
                            AnimationController.buttonPressSubtle(SettingsActivity.this, view);
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

                manager.updateProfile(playerName, selectedAvatar);
                updateProfileButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, manager.getPlayers().get(0).getPlayerProfileImage(SettingsActivity.this), null);
                settingsButton.setImageDrawable(manager.getPlayers().get(0).getPlayerProfileImage(SettingsActivity.this));
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
}
