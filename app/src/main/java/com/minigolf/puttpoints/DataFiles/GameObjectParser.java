package com.minigolf.puttpoints.DataFiles;

import android.content.Context;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.minigolf.puttpoints.Game;

import java.io.FileNotFoundException;
import java.io.FileReader;

class GameObjectParser {

    public Game createGame(String filename) {
        Gson gson = new Gson();
        Game game = null;
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(filename));
            game = gson.fromJson(String.valueOf(jsonReader), Game.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return game;
    }
}
