package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;

import java.util.ArrayList;

/**
 * Created by marij on 27.3.2017..
 */

class Player extends MovingUnit {

    private int playerLives;

    Player(Texture texture)
    {
        super(texture);

        this.playerLives = 4;
    }


    public void moveSensor(float x, float y)
    {

        //Gdx.app.log("LOGIRANJE", " " + (Math.toDegrees(Math.atan2(y, x)) -90));

        if(this.unitSprite.getRotation() != Math.toDegrees(Math.atan2(-x, y)))

            this.unitSprite.setRotation( (float) Math.toDegrees(Math.atan2(-x, y)));

        this.unitSprite.translate(x*2,y*2);
    }

    public void loseLife()
    {
        this.playerLives--;
        this.unitSprite.setCenter(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        this.unitSprite.setRotation(0);
    }

    public int getLives()
    {
        return this.playerLives;
    }


    public boolean collidedEnemy(ArrayList<Enemy> enemyList) {

        boolean check = false;

        for(int i = 0; i < enemyList.size(); i++)
        {
            if(Intersector.overlapConvexPolygons(this.getCollisionBox(), enemyList.get(i).getCollisionBox()))

                    check = true;
        }

        return check;
    }


    public boolean collidedEnemyBullet(ArrayList<Bullet> enemyBulletList) {

        boolean check = false;

        for(int i = 0; i < enemyBulletList.size(); i++)
        {
            if(Intersector.overlapConvexPolygons(this.getCollisionBox(), enemyBulletList.get(i).getCollisionBox()))

                check = true;
        }

        return check;
    }
}
