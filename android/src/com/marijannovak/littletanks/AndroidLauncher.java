package com.marijannovak.littletanks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.marijannovak.littletanks.LittleTanks;

public class AndroidLauncher extends AndroidApplication implements LittleTanks.GameOverCallback{

    private boolean soundCheck, sensorCheck;
    private int diff = 1;
    private String name;

	private LittleTanks game;

	private SharedPreferences sharedPreferences;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useAccelerometer = true;

		sharedPreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

		Intent startingIntent = this.getIntent();

		if(startingIntent.hasExtra(Constants.KEY_PLAYER))
		{
			name = startingIntent.getStringExtra(Constants.KEY_PLAYER);
		}

            soundCheck = sharedPreferences.getBoolean(Constants.Sound, false);
            sensorCheck = sharedPreferences.getBoolean(Constants.Sensors, false);
            diff = sharedPreferences.getInt(Constants.Difficulty, 1);


        if(name == null) name = "Unknown";

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
