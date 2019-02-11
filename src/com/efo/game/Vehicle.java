package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

public abstract class Vehicle {

    // Attributes ----------------------------------
    protected Vector pos;   // Position
    protected Vector oldPos;// Position im letzten Frame
    protected Vector gunPos;// Position der Waffe (Vorne)
    protected Vector vel;   // Geschwidigkeit
    protected Vector acc;   // Beschleunigung
    protected Image model;
    String faction = "";


    protected final double shootForce = 10.0;

    public static LinkedList<Projectile> empireLasers = new LinkedList<>();
    public static LinkedList<Projectile> republicLasers = new LinkedList<>();

    // Methods -------------------------------------

    public Vector getPos() {
        return this.pos;
    }

    public double getAbsVel() {
        return this.vel.getLength();
    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public void border() {

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


}
