package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.ImageTile;

public class Explosion {

    private final ImageTile explosion;
    private int frameCount, frame;
    private boolean finished = false;
    private Double spriteDuration;
    private float tempX, tempY;


    Explosion(int spriteCount, Double spriteDuration) {
        frame = 0;
        explosion = new ImageTile("/etc/explosion.png", 16,16);
        this.frameCount = spriteCount;
        this.spriteDuration = spriteDuration;

        this.frameCount = (int)(spriteCount * spriteDuration);
    }

    public void show(Renderer r, Vector pos) {
        r.drawImageTile(this.explosion,(int)pos.getX()-(explosion.getTileH()/2),(int)pos.getY()-(explosion.getTileH()/2), (int)this.tempX,(int)this.tempY);
    }

    public void update() {

        //Framework for explosion
        tempX += 1 / spriteDuration;
        if(tempX % 1 > 0) {
            frame++;
        }
        if (tempX > 4) {
            tempX = 0;
            tempY++;
            if (tempY > 3) {
                tempY = 0;
            }
        }

        if(frame == frameCount) {
            finished = true;
        }
    }

    boolean isFinished() {
        return finished;
    }
}
