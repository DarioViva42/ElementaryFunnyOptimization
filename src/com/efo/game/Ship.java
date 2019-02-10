package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Selfmade
public class Ship {
    // Attributes ----------------------------------
    protected Vector pos;   // Position
    protected Vector oldPos;// Position im letzten Frame
    protected Vector gunPos;// Position der Waffe (Vorne)
    protected Vector vel;   // Geschwidigkeit
    protected Vector acc;   // Beschleunigung
    protected Image falcon;

    protected double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    protected double alphaAcc;  // Die Winkelbeschleunigung
    protected double alpha;     // Der Winkel
    protected double shootForce = 10.0;

    public static LinkedList<Projectile> projectiles= new LinkedList<>();


    // Constructors --------------------------------
    Ship() {
        pos = new Vector(0,0,"c");
        oldPos = new Vector(0,0,"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = 0;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        falcon = new Image("/ship.png");
    }

    Ship(Vector pos, double alpha) {
        this.pos = pos;
        oldPos = new Vector(getX(),getY(),"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        falcon = new Image("/falcon.png");
    }

    // Methods -------------------------------------
    public void show(Renderer r){
      r.drawImage(falcon, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha));
    }

    public void turn(double turn) {
      this.alphaAcc = turn;
    }

  public void shoot() {
    projectiles.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10,this.alpha,"p"),true)),
            new Vector(this.shootForce, this.alpha, "p")));
  }

    public Vector getPos() {
        Vector position = this.pos;
        return position;
    }

    public void setBoost(double boost) {
      this.acc.setP(boost, this.alpha);
    }

    public double getAbsVel() {
        double absoluteVelocity = this.vel.getLength();
        return absoluteVelocity;
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
        this.vel.setP(0.95 * l, a);
        //Turning Drag
        this.alphaVel *= 0.95;
    }

    public void border() {

        if(this.pos.getX() < -10) {
            this.pos.setC(480 + 10,this.pos.getY());
        } else if(this.pos.getX() > (480 + 10)) {
            this.pos.setC(-10,this.pos.getY());

        } else if(this.pos.getY() < -10) {
            this.pos.setC(this.pos.getX(),320 +  10);

        }else if(this.pos.getY() > (320 + 10)) {
            this.pos.setC(this.pos.getX(), -10);
        }

    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }
}
