package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameOver extends Activity implements View.OnClickListener {

    private static final String KEY_SCORE = "key_score";
    private static final String KEY_PLAYER = "key_player";
    private static final String KEY_TIME = "key_time";
    private static final String KEY_KILLED = "key_killed";

    private String playerName;
    private int score, time, killed;

    TextView tvScore;
    Button btnPlayAgain, btnMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tvScore = (TextView) findViewById(R.id.tvScore);
        btnPlayAgain = (Button) findViewById(R.id.btnAgain);
        btnMain = (Button) findViewById(R.id.btnMain);

        btnPlayAgain.setOnClickListener(this);
        btnMain.setOnClickListener(this);

        Intent startingIntent = this.getIntent();

        if(startingIntent.hasExtra(KEY_PLAYER))
        {
            playerName = startingIntent.getStringExtra(KEY_PLAYER);
        }

        if(startingIntent.hasExtra(KEY_SCORE))
        {
            score = startingIntent.getIntExtra(KEY_SCORE, 0);
        }

        if(startingIntent.hasExtra(KEY_TIME))
        {
            time = startingIntent.getIntExtra(KEY_TIME, 0);
        }

        if(startingIntent.hasExtra(KEY_KILLED))
        {
            killed = startingIntent.getIntExtra(KEY_KILLED, 0);
        }

        DatabaseHelper.getInstance(this).addScore(new ScoreItem(playerName, score, time, killed));

        ArrayList<ScoreItem> testList = DatabaseHelper.getInstance(this).getHighScores();

        for(ScoreItem score : testList)
        {
            Log.d("DB LOG", "Name: " + score.getPlayerName() + " Score: " + score.getScore()
                    + " Time: " + score.getPlayTime() + " Killed: " + score.getKilled());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAgain:

                Intent againIntent = new Intent(this, AndroidLauncher.class);
                startActivity(againIntent);
                finish();

                break;

            case R.id.btnMain:

                Intent mainIntent = new Intent(this, MenuActivity.class);
                startActivity(mainIntent);
                finish();

                break;
        }
    }
}
