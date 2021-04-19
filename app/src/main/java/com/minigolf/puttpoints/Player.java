package com.minigolf.puttpoints;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Player {

    private final int playerID;
    private Drawable playerProfileImage;
    private String name;
    private String filename;


    //just for demo purposes until we have save functionality
    static ArrayList<Player> players = new ArrayList<>();

    public Player(Context c, String name) {
        UserPreferencesManager manager = new UserPreferencesManager(c);
        this.name = name;
        playerID = manager.getLastPlayerID();
        manager.updateLastPlayerID(playerID+1);
    }

    public Player(Context c, String name, Drawable playerProfileImage) {
        UserPreferencesManager manager = new UserPreferencesManager(c);
        this.name = name;
        this.playerProfileImage = playerProfileImage;
        playerID = manager.getLastPlayerID();
        manager.updateLastPlayerID(playerID+1);
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}