package com.minigolf.puttpoints;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Player {

    private static final int currentID = 0;
    private final int playerID;
    private Drawable playerProfileImage;
    private String name;


    //just for demo purposes until we have save functionality
    static ArrayList<Player> players = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        playerID = currentID + 1;
    }

    public Player(String name, Drawable playerProfileImage) {
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

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getPlayerProfileImage() {
        return playerProfileImage;
    }

    public void setPlayerProfileImage(Drawable playerProfileImage) {
        this.playerProfileImage = playerProfileImage;
    }

    @Override
    public boolean equals(Object o) {
        Player p = (Player)o;
        return (this.getPlayerID() == p.playerID);
    }

}