package com.example.androidgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidgameapp.Model.SaveState;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main game. This class will save the users scores upon game completion.
 * This class has two arrays, one for the buttons, and one for the state of the buttons.
 * The button array will either display a number or a bomb.
 * The integer will be 0 for unpressed, 1 for bomb, 2 for bomb found, 3 for scan completed
 * The users highscore will get replaced with the the current score if the surrent score is lower than the high score
 *
 */
public class TheGame extends AppCompatActivity {

    private int numSizeRow = -1;
    private int numSizeCol = -1;
    private int bombs  = -1;
    private int gamesPlayed = -1;
    private int scanCount = 0;
    private int bestScore = -1;
    private String gameConfig;
    private Vibrator vibrator;
    private Button buttons[][] = new Button[4][6];
    private int bombSpot[][] = new int[4][6];
    private int found = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        getSupportActionBar().hide();
        restoreData();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        buttons = new Button[numSizeRow][numSizeCol];
        bombSpot = new int[numSizeRow][numSizeCol];
        displayText();
        bombLocation();
        populateButtons();


    }

    private void displayText() {
        String totalbombs = getString(R.string.total_bombs);
        totalbombs += bombs;
        TextView textViewBombs = (TextView) findViewById(R.id.totalBombs);
        textViewBombs.setText(totalbombs);
        String scansused = getString(R.string.of_scans_used);
        scansused += scanCount;
        TextView textViewScans = (TextView) findViewById(R.id.scansUsed);
        textViewScans.setText(scansused);
        String bombsfound = getString(R.string.of_bombs_found);
        bombsfound += found;
        TextView textViewFound = (TextView) findViewById(R.id.bombsFound);
        textViewFound.setText(bombsfound);

        String s = "Total Games Finished: " + gamesPlayed + "\n" + gameConfig + "\nBest Score: " + bestScore;
        if (bestScore == -1) {
            s = "Total Games Finished: " + gamesPlayed + "\n" + gameConfig + "\nBest Score: Unplayed";
        }
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreData();
    }

    private void restoreData() {
        SharedPreferences sp = getSharedPreferences("MineSeekerSP", Context.MODE_PRIVATE);
        numSizeRow = sp.getInt("rows", 4);
        numSizeCol = sp.getInt("cols", 6);
        bombs = sp.getInt("bombCount", 6);
        gamesPlayed = sp.getInt("gamesPlayed", 0);
        gameConfig = "Size: " + numSizeRow + " x " + numSizeCol + " Bombs:" + bombs;
        bestScore = sp.getInt(gameConfig, -1);
    }
    private void saveData() {
        SharedPreferences sp = getSharedPreferences("MineSeekerSP", Context.MODE_PRIVATE);

        if(gamesPlayed != -1) {
            SaveState.getInstance().saveData(sp,"gamesPlayed", gamesPlayed);
        } else {
            SaveState.getInstance().saveData(sp,"gamesPlayed", 0);
        }
        if(bestScore != -1) {
            SaveState.getInstance().saveData(sp,gameConfig, bestScore);
        } else {
            SaveState.getInstance().saveData(sp,gameConfig, scanCount);
        }


    }



    private void populateButtons() {
        TableLayout table = (TableLayout) findViewById(R.id.playArea);

        for(int row = 0; row < numSizeRow; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col = 0; col < numSizeCol; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);

                        if(found == bombs) {
                            //Toast.makeText(getApplicationContext(), "" + scanCount + "<" + bestScore, Toast.LENGTH_SHORT).show();
                            gamesPlayed++;
                            if(scanCount < bestScore ) {
                                bestScore = scanCount;
                            }
                            saveData();
                            openDialog();
                        }
                    }
                });
                //bombUpdate();
                tableRow.addView(button);
                buttons[row][col] = button;

            }
        }
    }
    private void vibratePhone() {
       if(vibrator == null) return;
       vibrator.cancel();
        long[] pattern = {0,200,100};

        vibrator.vibrate(pattern, 0);
        new Timer().schedule(new TimerTask() {

            @Override
                public void run() {
                vibrator.cancel();
            }
        }, 500);
    }
    private void bombUpdate(int row, int col) {

        int updateBombs = 0;
        String bombsfound = getString(R.string.of_bombs_found);
        bombsfound += found;
        TextView textViewFound = (TextView) findViewById(R.id.bombsFound);
        textViewFound.setText(bombsfound);

        for(int i = 0; i < numSizeRow; i++) {
            //if(bombSpot[i][col] == 1) updateBombs++;
            if(bombSpot[i][col] == 3) {
                bombCheck(i, col);
            }
        }
        for(int j = 0; j < numSizeCol; j++) {
            //if(bombSpot[row][j] == 1) updateBombs++;
            if(bombSpot[row][j] == 3) {
                bombCheck(row, j);
            }
        }


    }

    private void openDialog() {
        CongratulationsDialog goodJob = new CongratulationsDialog();
        goodJob.show(getSupportFragmentManager(), "congrats");
        //goodJob.dismiss();
        //((Activity) this).finish();
        //finish();
    }

    public void bombLocation()
    {
        Random rand = new Random();
        int bombCount = 0;
        while (bombCount < bombs)
        {
            int randomInteger = (int) (rand.nextDouble() * buttons.length);
            int randomInteger2 = (int) (rand.nextDouble() * buttons[0].length);
            if (bombSpot[randomInteger][randomInteger2] == 1)
                continue;
            else
            {//
                bombSpot[randomInteger][randomInteger2] = 1;
                bombCount++;
            }
        }

    }
    private void scanInreaser() {
        scanCount++;

        String scansused = getString(R.string.of_scans_used);
        scansused += scanCount;
        TextView textView = (TextView) findViewById(R.id.scansUsed);
        textView.setText(scansused);
    }




    private void gridButtonClicked(int col, int row) {

        Button button = buttons[row][col];

        //Lock Button Sizes:
        lockButtonSizes();


        //button.setBackgroundResource(R.drawable.bomb_png_transparent_background_19);

        //Scale image
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dynamite);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();

        if(bombSpot[row][col] == 1) {
            //button.setBackgroundDrawable(new BitmapDrawable(resource, scaledBitmap));

            button.setBackground(new BitmapDrawable(resource, scaledBitmap));
           // button.setText("0");
            //button.setTextColor(getResources().getColor(R.color.colorLime));
            vibratePhone();
            found++;

            bombSpot[row][col] = 2;
            bombUpdate(row, col);
            button.setEnabled(true);
        } else {
            scanInreaser();
            bombCheck(row, col);

            bombSpot[row][col] = 3;
            button.setEnabled(false);
        }


    }

    private void bombCheck(int row, int col) {
        int bombInRNC = 0;

        for(int i = 0; i < numSizeRow; i++) {
            if(bombSpot[i][col] == 1) {
                bombInRNC++;

            }
        }
        for(int j = 0; j < numSizeCol; j++) {
            if(bombSpot[row][j] == 1) bombInRNC++;
        }
        buttons[row][col].setText("" +bombInRNC);
        buttons[row][col].setTextColor(getResources().getColor(R.color.colorLime));

    }

    private void lockButtonSizes() {
        for(int row = 0; row<numSizeRow; row++){
            for(int col = 0; col < numSizeCol; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }
}