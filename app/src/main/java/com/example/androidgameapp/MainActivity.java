package com.example.androidgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * This is the welcome screen. It can be skipped by pressing the skip button or waiting 4 seconds.
 * This activity will only execute at launch and when exiting out through the main menu it will not be executed
 */
public class MainActivity extends AppCompatActivity {
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        Button btnSkip = findViewById(R.id.skipWelcome);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent skip = new Intent(MainActivity.this, mainMenu.class);
                timer.cancel();
                openMainMenuActivity();

            }
        });


        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                openMainMenuActivity();
            }
        }, 4000);


    }

    private void openMainMenuActivity() {
        Intent skip = new Intent(MainActivity.this, MainMenu.class);
        startActivity(skip);
    }
}