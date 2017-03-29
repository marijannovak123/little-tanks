package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by marij on 27.3.2017..
 */

public class GameScreen implements Screen {
//TODO iz run! pogledat sta fali,..enemy, screenwrap..soudnovi..backgroud..grafike i buttoni za start, database

    final LittleTanks game;
    public final static String TAG = "LOGIRANJE";
    OrthographicCamera camera;

    Player tank;
    Controls controller;
    ArrayList<Rectangle> screenBounds;
    ArrayList<Bullet> bulletList;
    ShapeRenderer shapeRenderer;
    Texture gamebg;
    Sound fireSound;

    float gameTime = 0;
    private float lastFireTime = 0;
    int i = 0;

    public GameScreen(final LittleTanks game)
    {
        this.game = game;

        init();

    }

    private void init() {

        game.batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();

        gamebg = new Texture("gamebg.jpg");

        tank = new Player(new Texture("player.png"));
        tank.setScale(camera.viewportHeight/500);
        tank.setPosition(camera.viewportWidth/2, camera.viewportHeight/2);

        controller = new Controls(new Texture("joystick.png"), new Texture ("fire.png"), (int) camera.viewportWidth);
        controller.setControlsScale(camera.viewportHeight/500);
        controller.setJoystickPos(controller.getJoystickPos().x, controller.getJoystickPos().y);
        controller.setFirePos(controller.getFirePos().x, controller.getFirePos().y);

        screenBounds = new ArrayList<Rectangle>();
        screenBounds.add(new Rectangle(0, camera.viewportHeight - 10, camera.viewportWidth, 10));
        screenBounds.add(new Rectangle(0, 0, 10, camera.viewportHeight));
        screenBounds.add(new Rectangle(camera.viewportWidth - 10, 0, 10, camera.viewportHeight));
        screenBounds.add(new Rectangle(0, 0, camera.viewportWidth, 10));

        bulletList = new ArrayList<Bullet>();

        fireSound = Gdx.audio.newSound(Gdx.files.internal("firesound.wav"));

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameTime += delta;

        //Gdx.app.log(TAG, "Kut tenka je " + tank.getRotation());
        //Gdx.app.log(TAG, "Broj metaka: " + bulletList.size());

        handleInput();

        handleBullets();

        drawGame();

    }

    private void handleInput() {

        if(Gdx.input.isTouched()) {

            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(controller.getJoystickSprite().getBoundingRectangle().contains(touch.x, touch.y))
            {
                screenWrapTank();
                tank.move(controller.getJoystickAngle(touch));
            }

            if(controller.getFireSprite().getBoundingRectangle().contains(touch.x, touch.y)){

                if(gameTime-lastFireTime > 0.5 && bulletList.size() < 5)
                {
                    tank.fire(bulletList, new Bullet(new Texture("bullet.png")), camera.viewportHeight / 500);
                    fireSound.play(0.5f);
                    lastFireTime = gameTime;
                }

                else
                {
                    //TODO nope
                }
            }
        }
    }

    private void handleBullets() {

        for(int i = 0; i < bulletList.size(); i++)
        {
            bulletList.get(i).move(bulletList.get(i).getRotation());
            if(bulletList.get(i).isOutOfScreen(camera))
            {
                bulletList.get(i).getSprite().getTexture().dispose();
                bulletList.remove(bulletList.get(i));
            }

        }

    }

    private void drawGame() {
        game.batch.begin();
        game.batch.draw(gamebg, 0, 0, camera.viewportWidth, camera.viewportHeight);

        for(int i = 0; i < bulletList.size(); i++) bulletList.get(i).draw(game.batch);
        tank.draw(game.batch);
        controller.drawControls(game.batch);

        game.batch.end();

    }

    private void screenWrapTank() {
        if(tank.getPosition().x > camera.viewportWidth) tank.getSprite().setPosition(0, tank.getPosition().y);
        if(tank.getPosition().x < -tank.getSprite().getWidth()) tank.getSprite().setPosition(camera.viewportWidth, tank.getPosition().y);
        if(tank.getPosition().y > camera.viewportHeight) tank.getSprite().setPosition(tank.getPosition().x, 0);
        if(tank.getPosition().y < -tank.getSprite().getHeight()) tank.getSprite().setPosition(tank.getPosition().x, camera.viewportHeight);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }
}
