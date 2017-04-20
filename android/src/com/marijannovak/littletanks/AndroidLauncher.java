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

	LittleTanks game = new LittleTanks();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;

		game.setGameOverCallback(this);

		initialize(game, config);
	}

	@Override
	public void gameOver(int score)
	{
		Intent gameOverIntent = new Intent(this, GameOver.class);
		gameOverIntent.putExtra(KEY_SCORE, score);
		startActivity(gameOverIntent);
	}

}
