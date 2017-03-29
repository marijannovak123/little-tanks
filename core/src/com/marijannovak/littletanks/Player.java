package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.utils.Align;

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

    void fire(ArrayList<Bullet> list, Bullet bullet, float bulletScale)
    {
        list.add(bullet);
        bullet.setPosition(this.unitSprite.getX() + this.unitSprite.getWidth()/2, this.unitSprite.getY() + this.unitSprite.getHeight()/2);
        bullet.getSprite().setOriginCenter();
        bullet.rotateTo(this.unitSprite.getRotation());
        bullet.setScale(bulletScale);

    }

    public void loseLife()
    {

    }

    public void dead()
    {

    }




}
