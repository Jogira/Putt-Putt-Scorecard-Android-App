package com.example.minigolfapp;

import android.media.Image;

public class Player {

    private int currentID;
    private int playerID;
    private Image playerProfileImage;
    private String name;

    public Player(String name) {
        this.name = name;
        playerID = currentID + 1;
    }

    public Player(String name, Image playerProfileImage) {
        this.name = name;
        this.playerProfileImage = playerProfileImage;
        playerID = currentID + 1;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public Image getPlayerProfileImage() {
        return playerProfileImage;
    }

    public void setPlayerProfileImage(Image playerProfileImage) {
        this.playerProfileImage = playerProfileImage;
    }

    public void setName(String name) {
        this.name = name;
    }

}
