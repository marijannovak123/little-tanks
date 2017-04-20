package com.marijannovak.littletanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import jdk.nashorn.internal.codegen.CompilerConstants;

class LittleTanks extends Game {

	SpriteBatch batch;
	GameOverCallback gameOverCallback;

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
		public void gameOver(int score);
	}

	public void setGameOverCallback(GameOverCallback callback)
	{
		this.gameOverCallback = callback;
	}
}
