package com.minigolf.puttpoints;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstLaunchActivity extends AppCompatActivity {

    private AlertDialog name;
    private int selectedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        Button createProfile = findViewById(R.id.firstLaunchProfile);
        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileCreator();
            }
        });
    }

    public void openProfileCreator(){
        AlertDialog.Builder nameBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        final View namePopupView = getLayoutInflater().inflate(R.layout.popup_new_player, null);
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
                            AnimationController.buttonPressSubtle(FirstLaunchActivity.this, view);
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

                UserPreferencesManager manager = new UserPreferencesManager(FirstLaunchActivity.this);
                int playerID = manager.getLastPlayerID();
                manager.updateLastPlayerID(playerID+1);
                Player user = new Player(playerName, selectedAvatar, playerID);
                manager.addPlayer(user);

                name.dismiss();
                manager.updateFirstLaunch(false);
                goHome();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                name.dismiss();
            }
        });
    }


    private void goHome(){
        Intent homePage = new Intent(FirstLaunchActivity.this, MainActivity.class);
        startActivity(homePage);
        this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}