package com.example.minigolfapp;

import java.util.ArrayList;

public class Game {

    private boolean isActive;
    private boolean parsActive;
    private ArrayList<String> players;
    private int currentHole;
    private int numHoles;
    private int[] pars = null;

    public Game(ArrayList<String> players, int numHoles, int[] pars){
        if(parsActive)
            this.pars = pars;

        this.players = players;
        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public Game(ArrayList<String> players, int numHoles){
        if(parsActive)
            pars = new int[numHoles];

        this.players = players;
        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public int getCurrentHole() {
        return currentHole;
    }

    public void setNumHoles(int numHoles) {
        this.numHoles = numHoles;
    }

    public void setCurrentHole(int currentHole) {
        this.currentHole = currentHole;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}
