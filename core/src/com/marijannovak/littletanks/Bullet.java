package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import java.util.ArrayList;

/**
 * Created by marij on 27.3.2017..
 */

class Bullet extends MovingUnit {


    Bullet(Texture texture)
    {
        super(texture);
        setSpeed(10);
    }

    public void explode(ArrayList<Bullet> bulletList, Texture texture)//eksplozija
    {
        this.setTexture(texture);
        //todo collision bullet sound i tekstura za boom
    }




    public void setBulletScale(float scale)
    {
        this.unitSprite.setScale(scale);
    }

    @Override
    public void move(float angle) {

        this.unitSprite.translateX(-(float)Math.sin(this.unitSprite.getRotation()*Math.PI/180) * this.unitSpeed);
        this.unitSprite.translateY((float)Math.cos(this.unitSprite.getRotation()*Math.PI/180) * this.unitSpeed);

    }

    public boolean isOutOfScreen(Camera camera) {

        if(this.unitSprite.getX() > camera.viewportWidth || this.unitSprite.getX() < 0
                || this.unitSprite.getY() > camera.viewportHeight || this.unitSprite.getY() < 0) return true;
        else return false;
    }
}
