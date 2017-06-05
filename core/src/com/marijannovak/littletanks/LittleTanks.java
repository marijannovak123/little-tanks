package com.marijannovak.littletanks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class LittleTanks extends Game {

	SpriteBatch batch;
	GameOverCallback gameOverCallback;
	String playerName;
	int difficulty;
	boolean sound, sensor;

	public LittleTanks(String name, int diff, boolean snd, boolean snsr)
	{
		this.playerName = name;
		this.difficulty = diff;
		this.sound = snd;
		this. sensor = snsr;

	}

	@Override
	public void create () {

		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public interface GameOverCallback{

		public void gameOver(String player, int score, int time, int killed);
	}

	public void setGameOverCallback(GameOverCallback callback)
	{
		this.gameOverCallback = callback;
	}
}
