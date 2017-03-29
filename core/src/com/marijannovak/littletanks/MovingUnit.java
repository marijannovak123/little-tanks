
package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.marijannovak.littletanks.GameScreen.TAG;

/**
 * Created by marij on 27.3.2017..
 */

public class MovingUnit {

    public Sprite unitSprite;
    public int unitSpeed = 5;

    public MovingUnit(Texture texture) {
        this.unitSprite = new Sprite(texture);
    }

    public void setTexture(Texture texture)
    {
        this.unitSprite.setTexture(texture);
    }

    public void setPosition(float x, float y) {
        this.unitSprite.setCenter(x, y);
    }

    public Vector2 getPosition() {
        Vector2 position = new Vector2();
        position.x = this.unitSprite.getX();
        position.y = this.unitSprite.getY();
        return position;
    }

    public void move(float angle) {

        if(this.unitSprite.getRotation() != (angle -90)) this.unitSprite.setRotation(angle-90);

        /*Gdx.app.log(TAG, "predani angle = " + angle);
        Gdx.app.log(TAG, "predani angle rad = " + angle*Math.PI/180);
        Gdx.app.log(TAG, "cos(alfa)=" + Math.cos(angle*Math.PI/180) + " sin(alfa)=" + Math.sin(angle*Math.PI/180) );*/

        this.unitSprite.translateX(-(float)Math.sin(this.unitSprite.getRotation()*Math.PI/180) * this.unitSpeed);
        this.unitSprite.translateY((float)Math.cos(this.unitSprite.getRotation()*Math.PI/180) * this.unitSpeed);


    }

    public void draw(SpriteBatch batch) {
        unitSprite.draw(batch);
    }

    public float getRotation() {
        return  (this.unitSprite.getRotation() +90);
    }

    public void rotateTo(float angle)
    {
        this.unitSprite.setRotation(angle);
    }

    public void rotateBy(float angle)
    {
        this.unitSprite.rotate(angle);
    }

    public void setSprite(Sprite sprite)
    {
        this.unitSprite.set(sprite);
    }

    public Sprite getSprite()
    {
        return this.unitSprite;
    }

    public void setSpeed(int speed)
    {
        this.unitSpeed = speed;
    }

    public boolean checkWall(ArrayList<Rectangle> rectList)
    {
        boolean check =  false;

        for(int i = 0; i < rectList.size(); i++)
        {
            if(Intersector.overlaps(this.unitSprite.getBoundingRectangle(), rectList.get(i))) check = true;

        }

        return check;
    }

    public void setScale(float scale) {

        this.unitSprite.setScale(scale);
    }
}
