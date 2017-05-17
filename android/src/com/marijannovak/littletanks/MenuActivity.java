package com.marijannovak.littletanks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener{

    private static final String KEY_PLAYER = "key_player";
    private Button playButton;
    private Button scoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        playButton = (Button) findViewById(R.id.playBtn);
        scoreButton = (Button) findViewById(R.id.scoresBtn);

        playButton.setOnClickListener(this);
        scoreButton.setOnClickListener(this);
    }


//TODO database
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.playBtn:

                Intent playIntent = new Intent(this, AndroidLauncher.class);
                playIntent.putExtra(KEY_PLAYER, "Test Name");
                startActivity(playIntent);
                finish();

                break;

            case R.id.scoresBtn:

                Intent scoreIntent = new Intent(this, HighScores.class);
                startActivity(scoreIntent);
                finish();

                break;
        }
    }
}
