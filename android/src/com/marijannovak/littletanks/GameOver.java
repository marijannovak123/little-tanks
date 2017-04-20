package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends Activity implements View.OnClickListener {

    private static final String KEY_SCORE = "key_score";
    private int score;

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

        if(startingIntent.hasExtra(KEY_SCORE))
        {
            score = startingIntent.getIntExtra(KEY_SCORE, 0);
        }

        tvScore.setText("Your score: " + String.valueOf(score));
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
