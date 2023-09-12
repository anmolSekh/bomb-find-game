package com.example.androidgameapp.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

/**
 * This saves all of the data into shared preferences
 */
public class SaveState {
    private static SaveState instance;

    private SaveState() {}

    public static SaveState getInstance() {
        if(instance == null) {

            instance = new SaveState();
        }
        return instance;
    }
    public void saveData(SharedPreferences sp, String key, int value) {
        if(sp == null || key == null) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public int restoreData(SharedPreferences sp, String key, int defaultVal) {
        if(sp == null || key == null) return defaultVal;
        return sp.getInt(key, defaultVal);
    }
}
