package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.gfx.ImageTile;

public class Explosion {

    private final ImageTile explosion;
    private int spriteCount;
    boolean finished = false;
    Double spriteDuration;
    float tempX, tempY;


    Explosion(int spriteCount, Double spriteDuration) {
        explosion = new ImageTile("/explosion.png", 16,16);
        this.spriteCount = spriteCount;
        this.spriteDuration = spriteDuration;
    }

    public void show(Renderer r, int x, int y) {
        r.drawImageTile(this.explosion,x,y, (int)this.tempX,(int)this.tempY);
    }

    public void update() {

        //Framework for explosion
        tempX += 1 / spriteDuration;
        if(tempX % 1 > 0) {
            spriteCount--;
        }
        if (tempX > 4) {
            tempX = 0;
            tempY++;
            if (tempY > 3) {
                tempY = 0;
            }
        }

        if(spriteCount == 0) {
            finished = true;
        }
    }

    public void getFrame() {


    }
}
