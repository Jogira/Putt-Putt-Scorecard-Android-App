package com.minigolf.puttpoints;

import android.content.Context;

import java.util.ArrayList;

public class Game {

    private final int gameID;
    private boolean isActive;
    private boolean parsActive;
    private ArrayList<Player> players;

    //index = (hole number - 1), each int[] stores all player scores for that hole
    private ArrayList<int[]> playerScores;
    private int currentHole;
    private int numHoles;
    public int currentPlayerTurn = 0;
    private int[] pars;

    //holds the current game object. It is used to keep track of all information about the most relevant game at the time
    //current game object is either a newly created game, or a past game that a user is viewing
    public static Game currentGame = null;

    public Game(Context c, ArrayList<Player> players, int numHoles, int[] pars){
        this.pars = pars;

        UserPreferencesManager manager = new UserPreferencesManager(c);

        this.gameID = manager.getLastGameID();
        manager.updateLastGameID(gameID+1);
        this.playerScores = new ArrayList<>(players.size());

        int[] playerRow = new int[players.size()];
        for(int i = 0; i < players.size(); i++)
            playerRow[i] = Integer.MIN_VALUE;

        for(int i = 0; i < numHoles; i++)
            playerScores.add(playerRow);

        this.players = players;
        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public Game(Context c, ArrayList<Player> players, int numHoles){
        pars = new int[numHoles];

        UserPreferencesManager manager = new UserPreferencesManager(c);

        this.gameID = manager.getLastGameID();
        manager.updateLastGameID(gameID+1);

        this.players = players;
        this.playerScores = new ArrayList<>(players.size());

        for(int i = 0; i < numHoles; i++)
            playerScores.add(newPlayerRow());

        this.numHoles = numHoles;
        isActive = true;
        currentHole = 1;
    }

    public int[] newPlayerRow() {
        int[] playerRow = new int[players.size()];
        for(int i = 0; i < players.size(); i++)
            playerRow[i] = Integer.MIN_VALUE;

        return playerRow;
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
        //if active = false, save game to userPrefs
    }

    public ArrayList<int[]> getPlayerScores(){
        return playerScores;
    }

    public void setPlayerScore(int player, int playerScore) {
        int[] score = playerScores.get(getCurrentHole()-1);
        score[player] = playerScore;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setPars(int[] pars) {
        this.pars = pars;
    }

    public void setParAtHole(int hole, int par) {
        this.pars[hole] = par;
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

    public int getGameID() {
        return this.gameID;
    }

    @Override
    public boolean equals(Object o) {
        Game g = (Game)o;
        return (this.getGameID() == g.getGameID());
    }
}
