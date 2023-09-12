package com.example.androidgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * This will be opened upon leaving the welcome screen
 * There will be three possible options from here on:
 *  1.Play
 *      This will take you to the board where you can play the game with default or selected configuration
 *  2.Help
 *      This will take you to the help screen that has a basic rundown of the app
 *  3.Options
 *      This allows you to change the configuration and reset the history/records
 */
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        Button playBtn = findViewById(R.id.playGame);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainMenu.this, TheGame.class);
                startActivity(game);
            }
        });
        Button optionBtn = findViewById(R.id.optionsScreen);
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent options = new Intent(MainMenu.this,TheOptions.class);
                startActivity(options);
            }
        });
        Button helpBtn = findViewById(R.id.helpScreen);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent(MainMenu.this,TheHelp.class);
                startActivity(help);
            }
        });
    }


}