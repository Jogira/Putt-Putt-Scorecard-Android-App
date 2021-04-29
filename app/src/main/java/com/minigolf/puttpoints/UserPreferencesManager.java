package com.minigolf.puttpoints;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class UserPreferencesManager {

    private SharedPreferences userPrefs;
    private int lastPlayerID;
    private int lastGameID;
    private boolean usePars;
    private boolean firstLaunch;
    public ArrayList<Player> players;
    public ArrayList<Game> games;
    private static final String PREFS_NAME = "prefs";
    private static final String PREFS_PARS = "pars";
    private static final String PREFS_FIRST_LAUNCH = "first_launch";
    private static final String PREFS_PLAYERS = "players";
    private static final String PREFS_LAST_PLAYER_ID = "last_player_id";
    private static final String PREFS_LAST_GAME_ID = "last_game_id";
    private static final String PREFS_GAMES = "games";


    UserPreferencesManager(Context c) {
        userPrefs = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        lastPlayerID = userPrefs.getInt(PREFS_LAST_PLAYER_ID, 0);
        lastGameID = userPrefs.getInt(PREFS_LAST_GAME_ID, 0);
        usePars = userPrefs.getBoolean(PREFS_PARS, false);
        firstLaunch = userPrefs.getBoolean(PREFS_FIRST_LAUNCH, true);
        players = getPlayers();
        games = getGames();
    }

    void addGame(Game newGame) {
        SharedPreferences.Editor editor = userPrefs.edit();
        Gson gson = new Gson();

        games.remove(newGame);
        games.add(newGame);
        String gamesJson = gson.toJson(games);
        editor.putString(PREFS_GAMES, gamesJson);
        editor.apply();
        games = getGames();
    }

    void removeGame(Game game) {
        SharedPreferences.Editor editor = userPrefs.edit();
        Gson gson = new Gson();
        games.remove(game);
        String gamesJson = gson.toJson(games);
        editor.putString(PREFS_GAMES, gamesJson);
        editor.apply();
        games = getGames();
    }

    public void updatePars(boolean usePars) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(PREFS_PARS, usePars);
        editor.apply();
        usePars = userPrefs.getBoolean(PREFS_PARS, false);
    }


    public void updateFirstLaunch(boolean firstLaunch) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(PREFS_FIRST_LAUNCH, firstLaunch);
        editor.apply();
        firstLaunch = userPrefs.getBoolean(PREFS_FIRST_LAUNCH, true);
    }


    public void updateLastPlayerID(int id) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putInt(PREFS_LAST_PLAYER_ID, id);
        editor.apply();
        lastPlayerID = userPrefs.getInt(PREFS_LAST_PLAYER_ID, 0);
    }


    public void updateLastGameID(int id) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putInt(PREFS_LAST_GAME_ID, id);
        editor.apply();
        lastGameID = userPrefs.getInt(PREFS_LAST_GAME_ID, 0);
    }


    void removePlayer(Player player) {
        SharedPreferences.Editor editor = userPrefs.edit();
        Gson gson = new Gson();
        players.remove(player);
        String playersJson = gson.toJson(players);
        editor.putString(PREFS_PLAYERS, playersJson);
        editor.apply();
        players = getPlayers();
    }


    void addPlayer(Player newPlayer) {
        SharedPreferences.Editor editor = userPrefs.edit();
        Gson gson = new Gson();
        players.add(newPlayer);
        String playersJson = gson.toJson(players);
        editor.putString(PREFS_PLAYERS, playersJson);
        editor.apply();
        players = getPlayers();
    }


    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public boolean parsOn() {
        return usePars;
    }

    public int getLastPlayerID() {
        return lastPlayerID;
    }

    public int getLastGameID() {
        return lastGameID;
    }

    //Retrieves user's player profiles
    ArrayList<Player> getPlayers() {
        Gson gson = new Gson();
        String playersJson = userPrefs.getString(PREFS_PLAYERS, null);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        players = gson.fromJson(playersJson, type);

        if (players == null)
            players = new ArrayList<>();

        return players;
    }

    ArrayList<Game> getGames() {
        Gson gson = new Gson();
        String gameJson = userPrefs.getString(PREFS_GAMES, null);
        Type type = new TypeToken<ArrayList<Game>>() {
        }.getType();
        games = gson.fromJson(gameJson, type);
        if (games == null) {
            games = new ArrayList<>();
        }
        return games;
    }

}