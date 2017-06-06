package com.marijannovak.littletanks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.marijannovak.littletanks.LittleTanks;

public class AndroidLauncher extends AndroidApplication implements LittleTanks.GameOverCallback{




    private boolean soundCheck, sensorCheck;
    private int diff = 1;
    private String name;

	LittleTanks game;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useAccelerometer = true;

		Intent startingIntent = this.getIntent();

		if(startingIntent.hasExtra(Constants.KEY_PLAYER))
		{
			name = startingIntent.getStringExtra(Constants.KEY_PLAYER);
		}

		if(startingIntent.hasExtra(Constants.Sound))
        {
            soundCheck = startingIntent.getBooleanExtra(Constants.Sound, false);
        }

        if(startingIntent.hasExtra(Constants.Sensors))
        {
            sensorCheck = startingIntent.getBooleanExtra(Constants.Sensors, false);
        }

        if(startingIntent.hasExtra(Constants.Difficulty))
        {
            diff = startingIntent.getIntExtra(Constants.Difficulty, 1);
        }

        if(name == null) name = "Unknown";

		Log.d("Parametar log launcher", "name: " + name + " diff: "+ diff + " sound: " + soundCheck + " sensors: "+ sensorCheck);
		game = new LittleTanks(name, diff, soundCheck, sensorCheck);

		game.setGameOverCallback(this);

		initialize(game, config);
	}

	@Override
	public void gameOver(String player, int score, int time, int killed)
	{
		Intent gameOverIntent = new Intent(this, GameOver.class);

		gameOverIntent.putExtra(Constants.KEY_PLAYER, player);
		gameOverIntent.putExtra(Constants.KEY_SCORE, score);
		gameOverIntent.putExtra(Constants.KEY_TIME, time);
		gameOverIntent.putExtra(Constants.KEY_KILLED, killed);

		startActivity(gameOverIntent);
	}

}
