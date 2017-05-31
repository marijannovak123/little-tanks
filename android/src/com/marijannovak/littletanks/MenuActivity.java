package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends Activity implements View.OnClickListener{

    private static final String KEY_PLAYER = "key_player";
    private String loginName;
    private Button playButton, scoreButton;
    private ImageButton settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        playButton = (Button) findViewById(R.id.playBtn);
        scoreButton = (Button) findViewById(R.id.scoresBtn);
        settingsButton = (ImageButton) findViewById(R.id.btnSettings);

        playButton.setOnClickListener(this);
        scoreButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
    }


//TODO database
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.playBtn:

                Intent playIntent = new Intent(this, AndroidLauncher.class);
                playIntent.putExtra(KEY_PLAYER, "Marijan");
                startActivity(playIntent);
                finish();

                break;

            case R.id.scoresBtn:

                Intent scoreIntent = new Intent(this, HighScores.class);
                startActivity(scoreIntent);
                finish();

                break;

            case R.id.btnSettings:

                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                finish();

                break;

        }
    }
}
