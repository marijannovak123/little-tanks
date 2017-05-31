package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by marij on 27.3.2017..
 */

class Bullet extends MovingUnit {


    Bullet(Texture texture)
    {
        super(texture);
        setSpeed(20);
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
