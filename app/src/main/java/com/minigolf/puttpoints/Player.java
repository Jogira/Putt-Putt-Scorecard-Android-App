package com.minigolf.puttpoints;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Player {

    private final int playerID;
    private int imageID;
    private String name;
    private int wins;
    private int losses;
    private int pointsScored;
    private int gamesPlayed;


    public Player(String name, int imageID, int id) {
        this.name = name;
        this.imageID = imageID;
        playerID = id;
        this.losses = 0;
        this.wins = 0;
        this.gamesPlayed = 0;
        this.pointsScored = 0;
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

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public void incrementWins() {
        this.wins += 1;
    }

    public void incrementLosses() {
        this.losses += 1;
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public void incrementGamesPlayed(){
        this.gamesPlayed += 1;
    }

    public int getPointsScored(){
        return this.pointsScored;
    }

    public void addToPointsScored(int numPoints) {
        this.pointsScored += numPoints;
    }

    public Drawable getPlayerProfileImage(Context c) {
        int id = this.imageID;
        int resourceID = R.drawable.avatar0;

        if(id == 0)
            resourceID = R.drawable.avatar1;
        else if(id == 1)
            resourceID = R.drawable.avatar0;
        else if(id == 2)
            resourceID = R.drawable.avatar2;
        else if(id == 3)
            resourceID = R.drawable.avatar3;
        else if(id == 4)
            resourceID = R.drawable.avatar4;
        else if(id == 5)
            resourceID = R.drawable.avatar5;
        else if(id == 6)
            resourceID = R.drawable.avatar6;
        else if(id == 7)
            resourceID = R.drawable.avatar7;

        return c.getDrawable(resourceID);
    }

    public void setPlayerProfileImage(int imageID) {
        this.imageID = imageID;
    }

    @Override
    public boolean equals(Object o) {
        Player p = (Player)o;
        return (this.getPlayerID() == p.playerID);
    }

}