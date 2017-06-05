package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by marij on 27.3.2017..
 */

class Controls {

    private final Sprite joystickSprite;
    private final Sprite fireSprite;
    private Vector2 joystickCenter;


    public Controls(Texture joystickTexture, Texture fireTexture, int screenWidth)
    {
        this.joystickSprite = new Sprite(joystickTexture);
        this.fireSprite = new Sprite(fireTexture);

    }

    public float getJoystickAngle(Vector3 touchPoint)
    {
        float angle = (float) Math.toDegrees(Math.atan2(touchPoint.y - this.joystickCenter.y, touchPoint.x - this.joystickCenter.x));

        if(angle < 0)

            angle += 360;

        return angle;
    }


    public void drawControls(SpriteBatch batch)
    {
        joystickSprite.draw(batch);
        fireSprite.draw(batch);
    }

    public Sprite getJoystickSprite()
    {
        return this.joystickSprite;
    }

    public void setJoystickSize(float width, float height)
    {
        this.joystickSprite.setSize(width, height);
    }

    public void setFireSize(float width, float height)
    {
        this.fireSprite.setSize(width, height);
    }

    public void setJoystickPos(float x, float y)
    {
        this.joystickSprite.setPosition(x, y);
    }

    public Sprite getFireSprite()
    {
        return this.fireSprite;
    }

    public void setFirePos(float x, float y)
    {
        this.fireSprite.setPosition(x, y);
    }

    public void setJoystickCenter(Vector2 joystickCenter) {
        this.joystickCenter = joystickCenter;
    }
}

