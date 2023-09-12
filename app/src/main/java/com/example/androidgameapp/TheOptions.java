package com.example.androidgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.androidgameapp.Model.SaveState;

/**
 * The user gets to pick the configuration for how he/she wants the game to be played.
 * The user can also reset the number of games played and the highscore for each configuration.
 */
public class TheOptions extends AppCompatActivity {

    // If the user never picks any settings, these will stay -1
    // If the user picks a setting, this will be set to that value
    private int numSizeRow = -1;
    private int numSizeCol = -1;
    private int bombcC = -1;
    private int gamesPlayed = -1;
    private String gameConfig;
    private int bestScore = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_options);
        getSupportActionBar().hide();
        restoreData();
        bombCountOption();
        gameSizeOption();
        displayStuff();
        resetStuff();

    }

    private void resetStuff() {
        resetGamesPlayed();
        resetHighScores();
    }

    private void displayStuff() {
        String totalGames = getString(R.string.total_games_played);
        totalGames += gamesPlayed;
        TextView textViewPlayed = (TextView) findViewById(R.id.totalPlayed);
        textViewPlayed.setText(totalGames);
        String currentConfig = getString(R.string.current_configuration);
        currentConfig += gameConfig;
        TextView textViewConfig = (TextView) findViewById(R.id.config);
        textViewConfig.setText(currentConfig);
        String bestScored = getString(R.string.best_score);
        if(bestScore != -1) {
            bestScored += bestScore;
        } else {
            bestScored += " UNPLAYED";
        }
        TextView textViewBest = (TextView) findViewById(R.id.bestScore);
        textViewBest.setText(bestScored);
    }




    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
    private void resetGamesPlayed() {

        Button resetPlayed = findViewById(R.id.resetGamesPlayed);
        resetPlayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamesPlayed = 0;
                saveData();
                displayStuff();
            }
        });
    }

    private void resetHighScores() {
        Button resetScored = findViewById(R.id.resetScore);
        resetScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bestScore = -1;
                saveData();
                displayStuff();
            }
        });
    }
    private void bombCountOption() {
        RadioGroup bombs = (RadioGroup) findViewById(R.id.bombcount);
        int[] number_bombs = getResources().getIntArray(R.array.number_of_bombs);

        for(int i = 0; i < number_bombs.length; i++) {
            final int bombCount = number_bombs[i];

            RadioButton button = new RadioButton(this);
            button.setText(bombCount + " bombs");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bombcC = bombCount;
                    saveData();
                    restoreData();
                    displayStuff();
                }
            });


            bombs.addView(button);
        }
    }

    private void gameSizeOption() {
        RadioGroup gameSizes = (RadioGroup) findViewById(R.id.gameSize);
        int[] game_sizesR = getResources().getIntArray(R.array.game_size);
        int[] game_sizesC = {6,10,15};
        for(int j = 0; j < game_sizesR.length; j++) {
            final int rows = game_sizesR[j];
            final int cols = game_sizesC[j];
            RadioButton button2 = new RadioButton(this);
            button2.setText(rows + " rows by " + cols + " columns");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numSizeRow = rows;
                    numSizeCol = cols;
                    saveData();
                    restoreData();
                    displayStuff();
                }
            });
            gameSizes.addView(button2);
        }
    }

    // If the user picked a particular value then save that value otherwise save the default value
    // The check for whether the user picked a value or not is if the variable is still -1 or not.
    private void saveData() {
        SharedPreferences sp = getSharedPreferences("MineSeekerSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (numSizeRow != -1) {
            SaveState.getInstance().saveData(sp,"rows", numSizeRow);
        } else {
            SaveState.getInstance().saveData(sp,"rows", 4);
        }

        if (numSizeCol != -1) {
            SaveState.getInstance().saveData(sp,"cols", numSizeCol);
        } else {
            SaveState.getInstance().saveData(sp,"cols", 6);
        }

        if (bombcC != -1) {
            SaveState.getInstance().saveData(sp,"bombCount", bombcC);
        } else {
            SaveState.getInstance().saveData(sp,"bombCount", 6);
        }
        if(gamesPlayed != -1) {
            SaveState.getInstance().saveData(sp,"gamesPlayed", gamesPlayed);
        } else {
            SaveState.getInstance().saveData(sp,"gamesPlayed", 0);
        }
        SaveState.getInstance().saveData(sp,gameConfig, bestScore);


    }
    private void restoreData() {
        SharedPreferences sp = getSharedPreferences("MineSeekerSP", Context.MODE_PRIVATE);
        numSizeRow = SaveState.getInstance().restoreData(sp,"rows",4);
        numSizeCol = SaveState.getInstance().restoreData(sp,"cols", 6);
        bombcC = SaveState.getInstance().restoreData(sp,"bombCount", 6);
        gamesPlayed = SaveState.getInstance().restoreData(sp,"gamesPlayed", 0);
        gameConfig = "Size: " + numSizeRow + " x " + numSizeCol + " Bombs:" + bombcC;
        bestScore = SaveState.getInstance().restoreData(sp,gameConfig, -1);
    }
}