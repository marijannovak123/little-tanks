
package com.marijannovak.littletanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;



class MovingUnit {

    protected Sprite unitSprite;
    protected int unitSpeed = 10;


    MovingUnit(Texture texture) {
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

    public Sprite getSprite()
    {
        return this.unitSprite;
    }

    public void setSpeed(int speed)
    {
        this.unitSpeed = speed;
    }

    public void setScale(float scale) {

        this.unitSprite.setScale(scale);
    }

    public void speedUp(int speed)
    {
        this.unitSpeed += speed;
    }

    public Polygon getCollisionBox()
    {
        Polygon boundingPolygon = new Polygon();

        float [] vertices = {this.unitSprite.getX(),this.unitSprite.getY(),
                this.unitSprite.getX(), this.unitSprite.getY()+this.unitSprite.getHeight(),
                this.unitSprite.getX()+this.unitSprite.getWidth(), this.unitSprite.getY()+this.unitSprite.getHeight(),
                this.unitSprite.getX()+this.unitSprite.getWidth(), this.unitSprite.getY()};

        boundingPolygon.setVertices(vertices);
        boundingPolygon.setOrigin(this.unitSprite.getX() + this.unitSprite.getWidth()/2 ,this.unitSprite.getY() + this.unitSprite.getHeight()/2);
        boundingPolygon.setRotation(this.unitSprite.getRotation());

        return boundingPolygon;
    }

    void fire(ArrayList<Bullet> list, Bullet bullet, float bulletScale)
    {
        list.add(bullet);
        bullet.setPosition(this.unitSprite.getX() + this.unitSprite.getWidth()/2, this.unitSprite.getY() + this.unitSprite.getHeight()/2);
        bullet.getSprite().setOriginCenter();
        bullet.rotateTo(this.unitSprite.getRotation());
        bullet.setScale(bulletScale);

    }
}
