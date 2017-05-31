package com.marijannovak.littletanks;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.marijannovak.littletanks.LittleTanks;

public class AndroidLauncher extends AndroidApplication implements LittleTanks.GameOverCallback{

	private static final String KEY_SCORE = "key_score";
	private static final String KEY_PLAYER = "key_player";
	private static final String KEY_TIME = "key_time";
	private static final String KEY_KILLED = "key_killed";


	LittleTanks game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useAccelerometer = true;

		Intent startingIntent = this.getIntent();

		if(startingIntent.hasExtra(KEY_PLAYER))
		{
			game = new LittleTanks(startingIntent.getStringExtra(KEY_PLAYER));
		}
		else
		{
			game = new LittleTanks("Unknown");
		}

		game.setGameOverCallback(this);

		initialize(game, config);
	}

	@Override
	public void gameOver(String player, int score, int time, int killed)
	{
		Intent gameOverIntent = new Intent(this, GameOver.class);

		gameOverIntent.putExtra(KEY_PLAYER, player);
		gameOverIntent.putExtra(KEY_SCORE, score);
		gameOverIntent.putExtra(KEY_TIME, time);
		gameOverIntent.putExtra(KEY_KILLED, killed);

		startActivity(gameOverIntent);
	}

}
