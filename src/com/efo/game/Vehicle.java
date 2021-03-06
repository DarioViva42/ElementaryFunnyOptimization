//Our intellectual property

package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

public abstract class Vehicle {

    // Attributes ----------------------------------
    Vector pos;
    Vector oldPos;
    Vector vel;
    Vector acc;
    Image model;
    String faction = "";

    // explosion 0, laser 1-5
    static LinkedList<SoundClip> sounds = new LinkedList<>();

    final double shootForce = 14.0;

    static LinkedList<Projectile> empireLasers = new LinkedList<>();
    static LinkedList<Projectile> rebelLasers = new LinkedList<>();

    LinkedList<Explosion> explosions = new LinkedList<>();

    // Methods -------------------------------------

    Vector getPos() {
        return this.pos;
    }

    Vector getVel() {
        return vel;
    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public void show(Renderer r){}

    void border() {
        //If out of border left -> go in from the right
        if(pos.getX() < -20) {
            pos.setC(480 + 20,pos.getY());
            //If out of border to the right -> go int from the left
        } else if(this.pos.getX() > (480 + 20)) {
            pos.setC(-20,pos.getY());
            //If out of border at the top -> go in from the bottom
        } else if(pos.getY() < -20) {
            pos.setC(pos.getX(),320 +  20);
            //If out of border at the bottom -> go in from the top
        }else if(pos.getY() > (320 + 20)) {
            pos.setC(pos.getX(), -20);
        }
    }

    String getFaction() {
        return faction;
    }


}
