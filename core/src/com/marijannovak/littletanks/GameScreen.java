package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by marij on 27.3.2017..
 */

class GameScreen implements Screen {
//TODO iz run! pogledat sta fali,..enemy, ..soudnovi...., database, animacijacdead tank
//TODO enemye na pocetku initspawn, poslije drukcije..kad se ubije tenk opet init spawn
    private final LittleTanks game;
    private final static String TAG = "LOGIRANJE";
    private OrthographicCamera camera;

    private Player tank;
    private Controls controller;
    private ArrayList<Bullet> bulletList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Sprite> lifeSprites;

    private ShapeRenderer shapeRenderer;
    private Texture gamebg;
    private Texture playerTexture;
    private BitmapFont font;
    GlyphLayout layout;


    private Sound fireSound;
    private Sound shotSound;

    private float gameTime = 0;
    private float lastFireTime = 0;
    private float lastSpeedUp = 0;
    private float blockTankTime = 0;


    private Random rand;
    private boolean tankCanMove = true;

    private boolean [ ] spotTaken;
    private Vector2 [] enemySpots;

    private int numberOfEnemies = 4;
    private int score = 0;
    private int enemiesKilled = 0;


    public GameScreen(final LittleTanks game)
    {
        this.game = game;

        init();

    }

    private void init() {

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
        enemyList = new ArrayList<Enemy>();

        fireSound = Gdx.audio.newSound(Gdx.files.internal("firesound.wav"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));

        rand = new Random();

        spotTaken = new boolean[4];
        enemySpots = new Vector2[4];
        enemySpots[0] = new Vector2(camera.viewportWidth/4, camera.viewportHeight/3);
        enemySpots[1] = new Vector2(camera.viewportWidth/4, camera.viewportHeight*2/3);
        enemySpots[2] = new Vector2(camera.viewportWidth*3/4, camera.viewportHeight*2/3);
        enemySpots[3] = new Vector2(camera.viewportWidth*3/4, camera.viewportHeight*3);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Gdx.app.log(TAG, "Lives: " + tank.getLives());
        Gdx.app.log(TAG, "Score : " + this.score);

        if(tank.getLives() == 0) game.gameOverCallback.gameOver(game.playerName, score, (int) gameTime, enemiesKilled); //TODO OVDJE DISPOSE SVEGA

        gameTime += delta;

        updateScore(1);

        //Gdx.app.log(TAG, "Kut tenka je " + tank.getRotation());
        //Gdx.app.log(TAG, "Broj metaka: " + bulletList.size());

        spawnEnemies();

        checkTankEnemyCollision();

        if(!tankCanMove && (gameTime - blockTankTime) > 0.5)
        {
            tankCanMove = true;
        }

        removeBulletKillEnemy();

        speedUpEnemies();

        moveEnemies();

        for(int i = 0; i < numberOfEnemies; i++) if(enemyList.size() == numberOfEnemies) spotTaken[i] = false;

        moveBullets();

        if(tankCanMove) handleInput();

        drawGame();

    }

    private void updateScore(float addedScore) {

        this.score += addedScore;
    }

    private void checkTankEnemyCollision() {

        if(tank.didCollide(enemyList))
        {
            tank.loseLife();

            if(lifeSprites.size() >= 1) lifeSprites.remove(lifeSprites.size() - 1);
            //TODO ANIMACIJA? ZVUK?
            tankCanMove = false;

            blockTankTime = gameTime;
        }
    }

    private void speedUpEnemies() {

        if(gameTime - lastSpeedUp > 20)
        {
            for(Enemy enemy : enemyList)
            {
                enemy.speedUp(1);
            }

            lastSpeedUp = gameTime;
        }
    }

    private void moveEnemies() {

        for(Enemy enemy : enemyList) //kreci neprijatelje
        {
            enemy.move(enemy.getRotation());

            if(enemy.getPosition().x > camera.viewportWidth)enemy.getSprite().setPosition(0, enemy.getPosition().y);
            if(enemy.getPosition().x < -enemy.getSprite().getWidth()) enemy.getSprite().setPosition(camera.viewportWidth, enemy.getPosition().y);
            if(enemy.getPosition().y > camera.viewportHeight) enemy.getSprite().setPosition(enemy.getPosition().x, 0);
            if(enemy.getPosition().y < -enemy.getSprite().getHeight()) enemy.getSprite().setPosition(enemy.getPosition().x, camera.viewportHeight);
        }
    }

    private void removeBulletKillEnemy() {

        for (int i = 0; i < enemyList.size(); i++) //ukloni upucane neprijatelje i metak koji je upucao
        {

            if(enemyList.get(i).isShot(bulletList) >= 0) {

                //Gdx.app.log(TAG, "shot");

                int whichShot = enemyList.get(i).isShot(bulletList);
                enemyList.get(i).getSprite().getTexture().dispose();
                enemyList.remove(i);
                bulletList.get(whichShot).getSprite().getTexture().dispose();
                bulletList.remove(whichShot);
                shotSound.play();

                enemiesKilled++;
                updateScore(50);
            }


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
            enemyList.get(enemyList.size()-1).setSpeed(1);

        }
    }

    private void handleInput() {

        for(int i = 0; i < 2; i ++) {

            if (Gdx.input.isTouched(i)) {


                Vector3 touch = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touch);

                if (controller.getJoystickSprite().getBoundingRectangle().contains(touch.x, touch.y)) {
                    screenWrapTank();
                    tank.move(controller.getJoystickAngle(touch));
                }

                if (controller.getFireSprite().getBoundingRectangle().contains(touch.x, touch.y)) {

                    if (gameTime - lastFireTime > 0.5 && bulletList.size() < 5) {
                        tank.fire(bulletList, new Bullet(new Texture("bullet.png")), camera.viewportHeight / 500);
                        fireSound.play(0.5f);
                        lastFireTime = gameTime;
                    } else {
                        //TODO nope
                    }
                }
            }

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

    }

    private void drawGame() {

        game.batch.begin();

        game.batch.draw(gamebg, 0, 0, camera.viewportWidth, camera.viewportHeight);

        for(Bullet bullet : bulletList) {bullet.draw(game.batch);}

        tank.draw(game.batch);

        for(Enemy enemy : enemyList){enemy.draw(game.batch);}

        for (Sprite lifeSprite : lifeSprites) lifeSprite.draw(game.batch);

        font.draw(game.batch, "Score: " + String.valueOf(this.score),0, camera.viewportHeight);

        controller.drawControls(game.batch);

        game.batch.end();

      //  drawCollisionRects();


    }

    private void drawCollisionRects() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.polygon(tank.getBoundingPolygon().getTransformedVertices());

        for (Enemy enemy : enemyList) {
            shapeRenderer.polygon(enemy.getBoundingPolygon().getTransformedVertices());  }

        shapeRenderer.end();
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
