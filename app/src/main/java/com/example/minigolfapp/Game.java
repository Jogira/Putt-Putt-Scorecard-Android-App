package com.example.minigolfapp;

import java.util.ArrayList;

public class Game {

    private static int CURRENT_GAME_ID = 0;
    private int gameID;
    private boolean isActive;
    private boolean parsActive;
    private ArrayList<Player> players;
    private int currentHole;
    private int numHoles;
    private int[] pars = null;

    public Game(ArrayList<Player> players, int numHoles, int[] pars){
        if(parsActive)
            this.pars = pars;

        this.gameID = CURRENT_GAME_ID + 1;
        CURRENT_GAME_ID = gameID;
        this.players = players;
        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public Game(ArrayList<Player> players, int numHoles){
        if(parsActive)
            pars = new int[numHoles];

        this.gameID = CURRENT_GAME_ID + 1;
        CURRENT_GAME_ID = gameID;
        this.players = players;
        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public int getCurrentHole() {
        return currentHole;
    }

    public void setCurrentHole(int currentHole) {
        this.currentHole = currentHole;
    }

    public void setNumHoles(int numHoles) {
        this.numHoles = numHoles;
    }

    public int getNumHoles() {
        return numHoles;
    }

    public void setActive(boolean active) {
        isActive = active;
        //if active = false, save game as csv file
    }

    public void setPars(int[] pars) {
        this.pars = pars;
    }

    public int[] getPars() {
        return pars;
    }

    public void setParsActive(boolean parsActive) {
        this.parsActive = parsActive;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public void removePlayer(String playerID) {
        this.players.remove(playerID);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
