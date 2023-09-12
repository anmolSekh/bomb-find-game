package com.example.androidgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Not really used, everything is done in the xml
 * About me feature my name the class and the hyperlink
 * How to play explains the game
 * and citation has the picture for the bomb, the trophy, the cloud (respectively)
 */
public class TheHelp extends AppCompatActivity {
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_help);
        getSupportActionBar().hide();
        Intent intoHelp = getIntent();
    }
//
}