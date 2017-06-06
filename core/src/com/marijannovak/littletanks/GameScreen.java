package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by marij on 27.3.2017..
 */

//TODO ENEMY BULLET PLAYER COLLISION
class GameScreen implements Screen {

    private final LittleTanks game;
    private final static String TAG = "LOGIRANJE";
    private OrthographicCamera camera;

    private Player tank;
    private Controls controller;
    private ArrayList<Bullet> bulletList;
    private ArrayList<Bullet> enemyBulletList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Sprite> lifeSprites;

    private ShapeRenderer shapeRenderer;
    private Texture gamebg;
    private Texture playerTexture;
    private BitmapFont font;


    private Sound fireSound;
    private Sound enemyFireSound;
    private Sound shotSound;
    private Sound tankDead;

    private float gameTime = 0;
    private float lastFireTime = 0;
    private float enemyLastFireTime = 0;
    private float lastSpeedUp = 0;
    private float blockTankTime = 0;


    private Random rand;
    private boolean tankCanMove = true;

    private boolean [ ] spotTaken;
    private Vector2 [] enemySpots;

    private int numberOfEnemies = 4;
    private int score = 0;
    private int enemiesKilled = 0;
    private int enemySpeed = 5;
    private double enemyShootSpeed = 0.4;


    public GameScreen(final LittleTanks game)
    {
        this.game = game;

        init();

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Gdx.app.log(TAG, "Lives: " + tank.getLives());
        //Gdx.app.log(TAG, "Score : " + this.score);
        //Gdx.app.log(TAG, "Kut tenka je " + tank.getRotation());
        //Gdx.app.log(TAG, "Broj metaka: " + bulletList.size());

        //Gdx.app.log(TAG, "Accelerometer X: " + Gdx.input.getAccelerometerX());
        //Gdx.app.log(TAG, "Accelerometer Y: " + Gdx.input.getAccelerometerY());
        //Gdx.app.log(TAG, "Accelerometer Z: " + Gdx.input.getAccelerometerZ());

        if(tank.getLives() == 0)

            game.gameOverCallback.gameOver(game.playerName, score, (int) gameTime, enemiesKilled);


        gameTime += delta;

        updateScore(1);

        spawnEnemies();

        handlePlayerCollision();

        if(!tankCanMove && (gameTime - blockTankTime) > 0.5)

            tankCanMove = true;

        handleEnemyCollision();

        speedUpEnemies();

        moveEnemies();

        for(int i = 0; i < numberOfEnemies; i++)

            if(enemyList.size() == numberOfEnemies)

                spotTaken[i] = false;

        moveBullets();

        if(tankCanMove)

            handleInput();

        enemyShoot();

        drawGame();

    }

    private void init() {

        enemySpeed += 2 * game.difficulty;
        enemyShootSpeed += game.difficulty/5;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();

        playerTexture = new Texture("player.png");
        gamebg = new Texture("gamebg.jpg");

        tank = new Player(playerTexture);
        tank.setScale(camera.viewportHeight/500);
        tank.setPosition(camera.viewportWidth/2, camera.viewportHeight/2);


        lifeSprites = new ArrayList<Sprite>();

        for(int i = 0; i < tank.getLives(); i++)
        {
            lifeSprites.add(new Sprite(playerTexture));
            lifeSprites.get(i).setSize(camera.viewportWidth/40, camera.viewportHeight/20);

            if(i == 0) {

                lifeSprites.get(i).setPosition(lifeSprites.get(i).getWidth()*0.2f, camera.viewportHeight - lifeSprites.get(i).getHeight()*2);
            }
            else
            {
                lifeSprites.get(i).setPosition(lifeSprites.get(i-1).getX() + lifeSprites.get(i).getWidth()*1.2f, camera.viewportHeight - lifeSprites.get(i).getHeight()*2);

            }
        }

        font = new BitmapFont();
        font.getData().setScale(camera.viewportHeight/300);


        controller = new Controls(new Texture("joystick.png"), new Texture ("fire.png"), (int) camera.viewportWidth);
        controller.setJoystickSize(camera.viewportHeight/3, camera.viewportHeight/3);
        controller.setFireSize(camera.viewportHeight/3, camera.viewportHeight/3);
        controller.setJoystickCenter(new Vector2(controller.getJoystickSprite().getWidth()/2, controller.getJoystickSprite().getHeight()/2));

        controller.setJoystickPos(0,0);
        controller.setFirePos(camera.viewportWidth - controller.getFireSprite().getWidth(), 0);

        bulletList = new ArrayList<Bullet>();
        enemyBulletList = new ArrayList<Bullet>();
        enemyList = new ArrayList<Enemy>();

        fireSound = Gdx.audio.newSound(Gdx.files.internal("firesound.wav"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
        tankDead = Gdx.audio.newSound(Gdx.files.internal("tank_dead.wav"));
        enemyFireSound = Gdx.audio.newSound(Gdx.files.internal("enemy_firesound.wav"));

        rand = new Random();

        spotTaken = new boolean[4];
        enemySpots = new Vector2[4];
        enemySpots[0] = new Vector2(camera.viewportWidth/4, camera.viewportHeight/3);
        enemySpots[1] = new Vector2(camera.viewportWidth/4, camera.viewportHeight*2/3);
        enemySpots[2] = new Vector2(camera.viewportWidth*3/4, camera.viewportHeight*2/3);
        enemySpots[3] = new Vector2(camera.viewportWidth*3/4, camera.viewportHeight*3);

    }

    private void enemyShoot() {

        if((gameTime - enemyLastFireTime) > 1/enemyShootSpeed && enemyBulletList.size() < 5 && enemyList.size() == 4)
        {
            enemyList.get(rand.nextInt(4)).fire(enemyBulletList, new Bullet(new Texture("bullet_enemy.png")), camera.viewportHeight / 500);

            if(game.sound) enemyFireSound.play(0.5f);

            enemyLastFireTime = gameTime;
        }
    }

    private void updateScore(float addedScore) {

        this.score += addedScore;
    }

    private void handlePlayerCollision() {

        if(tank.collidedEnemy(enemyList) || tank.collidedEnemyBullet(enemyBulletList))
        {
            tank.loseLife();

            enemyList.clear();

            enemySpeed = 5;
            enemyShootSpeed = 0.4;

            spawnEnemies();

            if(lifeSprites.size() >= 1) lifeSprites.remove(lifeSprites.size() - 1);

            if(game.sound) tankDead.play();
            Gdx.input.vibrate(200);
            tankCanMove = false;

            blockTankTime = gameTime;
        }

    }

    private void handleEnemyCollision() {

        for (int i = 0; i < enemyList.size(); i++)
        {

            if(enemyList.get(i).isShot(bulletList) >= 0) {

                //Gdx.app.log(TAG, "shot");

                int whichShot = enemyList.get(i).isShot(bulletList);
                enemyList.get(i).getSprite().getTexture().dispose();
                enemyList.remove(i);
                bulletList.get(whichShot).getSprite().getTexture().dispose();
                bulletList.remove(whichShot);
                if(game.sound) shotSound.play();

                enemiesKilled++;
                updateScore(200);
            }

        }
    }

    private void speedUpEnemies() {

        if(gameTime - lastSpeedUp > 15)
        {
            for(Enemy enemy : enemyList)
            {
                enemy.speedUp(1);
                enemySpeed++;

                enemyShootSpeed += 0.1;
            }

            lastSpeedUp = gameTime;
        }
    }

    private void moveEnemies() {

        for(Enemy enemy : enemyList) //kreci neprijatelje
        {
            enemy.move(enemy.getRotation());

            if(enemy.getPosition().x > camera.viewportWidth)

                enemy.getSprite().setPosition(0, enemy.getPosition().y);

            if(enemy.getPosition().x < -enemy.getSprite().getWidth())

                enemy.getSprite().setPosition(camera.viewportWidth, enemy.getPosition().y);

            if(enemy.getPosition().y > camera.viewportHeight)

                enemy.getSprite().setPosition(enemy.getPosition().x, 0);

            if(enemy.getPosition().y < -enemy.getSprite().getHeight())

                enemy.getSprite().setPosition(enemy.getPosition().x, camera.viewportHeight);
        }
    }



    private void spawnEnemies() {

        if(enemyList.size() < numberOfEnemies && enemyList.size() >= 0) //stvori neprijatelje
        {
            enemyList.add(new Enemy(new Texture("enemy.png")));

            int i;

            for(i = 0; i < numberOfEnemies; i++) {

                if (!spotTaken[i])
                {
                    spotTaken[i] = true;
                    break;
                }

            }

            enemyList.get(enemyList.size()-1).setPosition(enemySpots[i].x, enemySpots[i].y);
            enemyList.get(enemyList.size()-1).setScale(camera.viewportHeight/600);
            enemyList.get(enemyList.size()-1).rotateTo(rand.nextInt(360));
            enemyList.get(enemyList.size()-1).setSpeed(enemySpeed);

        }
    }

    private void handleInput() {

        screenWrapTank();

        if(game.sensor) sensorMove();

        for(int i = 0; i < 2; i ++) {

            if (Gdx.input.isTouched(i)) {

                Vector3 touch = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touch);

                if (controller.getJoystickSprite().getBoundingRectangle().contains(touch.x, touch.y)) {
                    tank.move(controller.getJoystickAngle(touch));
                }

                if (controller.getFireSprite().getBoundingRectangle().contains(touch.x, touch.y)) {

                    if (gameTime - lastFireTime > 0.5 && bulletList.size() < 5) {
                        tank.fire(bulletList, new Bullet(new Texture("bullet.png")), camera.viewportHeight / 500);
                        if(game.sound) fireSound.play(0.5f);
                        lastFireTime = gameTime;
                    } else {
                        //TODO nope
                    }
                }

            }

        }
    }

    private void sensorMove() {

     if(Gdx.input.isTouched()) {

                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if(!controller.getJoystickSprite().getBoundingRectangle().contains(touch.x, touch.y))
                {
                    if(Gdx.input.getAccelerometerX() > 0)

                        tank.moveSensor(Gdx.input.getAccelerometerY() , -Gdx.input.getAccelerometerX() + 5);

                    else if(Gdx.input.getAccelerometerX() < 0)

                        tank.moveSensor(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX());
                }
            }

            else
            {
                if(Gdx.input.getAccelerometerX() > 0)

                tank.moveSensor(Gdx.input.getAccelerometerY() , -Gdx.input.getAccelerometerX() + 5);

                else if(Gdx.input.getAccelerometerX() < 0)

                tank.moveSensor(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX() + 8);
            }

        }

    private void moveBullets() {

        for(int i = 0; i < bulletList.size(); i++)
        {
            bulletList.get(i).move(bulletList.get(i).getRotation());

            if(bulletList.get(i).isOutOfScreen(camera))
            {
                bulletList.get(i).getSprite().getTexture().dispose();
                bulletList.remove(i);
            }


        }

        for(int i = 0; i < enemyBulletList.size(); i++)
        {
            enemyBulletList.get(i).move(enemyBulletList.get(i).getRotation());

            if(enemyBulletList.get(i).isOutOfScreen(camera))
            {
                enemyBulletList.get(i).getSprite().getTexture().dispose();
                enemyBulletList.remove(i);
            }
        }

    }

    private void drawGame() {

        game.batch.begin();

        game.batch.draw(gamebg, 0, 0, camera.viewportWidth, camera.viewportHeight);

        for(Bullet bullet : bulletList) {bullet.draw(game.batch);}

        for(Bullet enemyBullet : enemyBulletList) {enemyBullet.draw(game.batch);}

        tank.draw(game.batch);

        for(Enemy enemy : enemyList){enemy.draw(game.batch);}

        for (Sprite lifeSprite : lifeSprites) lifeSprite.draw(game.batch);

        font.draw(game.batch, "Score: " + String.valueOf(this.score), 0, camera.viewportHeight);

        controller.drawControls(game.batch);

        game.batch.end();

        //drawCollisionRects();


    }

    private void drawCollisionRects() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.polygon(tank.getCollisionBox().getTransformedVertices());

        for (Enemy enemy : enemyList)

            shapeRenderer.polygon(enemy.getCollisionBox().getTransformedVertices());

        shapeRenderer.end();
    }

    private void screenWrapTank() {

        if(tank.getPosition().x > camera.viewportWidth)

            tank.getSprite().setPosition(0, tank.getPosition().y);

        if(tank.getPosition().x < -tank.getSprite().getWidth())

            tank.getSprite().setPosition(camera.viewportWidth, tank.getPosition().y);

        if(tank.getPosition().y > camera.viewportHeight)

            tank.getSprite().setPosition(tank.getPosition().x, 0);

        if(tank.getPosition().y < -tank.getSprite().getHeight())

            tank.getSprite().setPosition(tank.getPosition().x, camera.viewportHeight);
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
        //TODO DISPOSE
    }

    @Override
    public void show() {

    }
}
