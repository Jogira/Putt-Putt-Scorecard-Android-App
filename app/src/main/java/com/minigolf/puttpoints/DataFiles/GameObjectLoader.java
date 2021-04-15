package com.minigolf.puttpoints.DataFiles;

import com.google.gson.Gson;
import com.minigolf.puttpoints.Game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


class GameObjectLoader {
    public String filename;


    public String convertToString(Game game) {
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(game);
        return json;
    }

    public boolean loadIntoFile(Context context, Game game, String json) {
        filename = game.getFileName();
        try {
            FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
            if (json != null) {
                fos.write(json.getBytes());
            }
            fos.close();
            return true;
        } catch (IOException fileNotFound) {
            return false;
        }

    }
}
