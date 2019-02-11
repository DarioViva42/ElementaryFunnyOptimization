package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Selfmade
public class Ship extends Vehicle{

    protected double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    protected double alphaAcc;  // Die Winkelbeschleunigung
    protected double alpha;     // Der Winkel


    // Constructors --------------------------------
    Ship() {
        faction = "republic";

        pos = new Vector(0,0,"c");
        oldPos = new Vector(0,0,"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = 0;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        model = new Image("/ship.png");
    }

    Ship(Vector pos, double alpha) {
        this.pos = pos;
        oldPos = new Vector(getX(),getY(),"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        model = new Image("/falcon.png");
    }

    public void update(){
        vel.add(acc);
        oldPos.setC(pos.getX(), pos.getY());
        pos.add(vel);
        alphaVel += alphaAcc;
        alpha = (alpha + alphaVel) % 360;

        //drag
        double l = this.vel.getLength();
        double a = this.vel.getAngle();
        //Velocity Drag
        this.vel.setP(0.985 * l, a);
        //Turning Drag
        this.alphaVel *= 0.95;
    }

    public void turn(double turn) {
        this.alphaAcc = turn;
    }

    public void show(Renderer r){
        r.drawImage(model, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha));
    }

    public void setBoost(double boost) {
        this.acc.setP(boost, this.alpha);
    }

    public void shoot() {
        if(this.faction.equals("republic")) {
            Vehicle.republicLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
                    new Vector(this.shootForce, alpha, "p")));
        } else {
            Vehicle.empireLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
                    new Vector(this.shootForce, alpha, "p")));
        }
    }
}
