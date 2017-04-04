package com.marijannovak.littletanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;

import java.util.ArrayList;

/**
 * Created by marij on 30.3.2017..
 */

class Enemy extends MovingUnit {

    public Enemy(Texture texture) {
        super(texture);
    }

    public int isShot(ArrayList<Bullet> bulletList) //vraca koji metak ga je pogodio
    {
        int check = -1;

        for(int i = 0; i < bulletList.size(); i++)
        {
            if(Intersector.overlapConvexPolygons(this.getBoundingPolygon(), bulletList.get(i).getBoundingPolygon()))
            {
                check = i;
            }
        }

        return check;
    }
}
