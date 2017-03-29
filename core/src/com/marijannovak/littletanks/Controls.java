package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.awt.Point;

/**
 * Created by marij on 27.3.2017..
 */

public class Controls {

    private Sprite joystickSprite;
    private Sprite fireSprite;
    Vector2 joystickCenter;
    Vector2 fireCenter;


    public Controls(Texture joystickTexture, Texture fireTexture, int screenWidth)
    {
        this.joystickSprite = new Sprite(joystickTexture);
        this.fireSprite = new Sprite(fireTexture);
        this.joystickCenter = new Vector2(this.joystickSprite.getWidth()*1.2f, this.joystickSprite.getHeight()*1.2f);
        this.fireCenter = new Vector2(screenWidth - this.fireSprite.getWidth()*1.2f, this.fireSprite.getHeight()*1.2f);

    }

    public float getJoystickAngle(Vector3 touchPoint)
    {
        float angle = (float) Math.toDegrees(Math.atan2(touchPoint.y - this.joystickCenter.y, touchPoint.x - this.joystickCenter.x));


        if(angle < 0){
            angle += 360;
        }

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

    public void setControlsScale(float scale)
    {
        this.joystickSprite.setScale(scale);
        this.fireSprite.setScale(scale);
    }

    public void setJoystickPos(float x, float y)
    {
        this.joystickSprite.setCenter(x, y);
    }

    public Vector2 getJoystickPos()
    {
        return this.joystickCenter;
    }

    public Sprite getFireSprite()
    {
        return this.fireSprite;
    }

    public void setFirePos(float x, float y)
    {
        this.fireSprite.setCenter(x, y);
    }

    public Vector2 getFirePos()
    {
        return this.fireCenter;
    }
}
