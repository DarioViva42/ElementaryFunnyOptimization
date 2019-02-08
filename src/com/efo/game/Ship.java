package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Selfmade
public class Ship {
    // Attributes ----------------------------------
    protected Vector pos;   // Position
    protected Vector gunPos;// Position der Waffe (Vorne)
    protected Vector vel;   // Geschwidigkeit
    protected Vector acc;   // Beschleunigung
    protected Image falcon;

    private boolean isTurningL = false;
    private boolean isTurningR = false;
    private boolean isBoosting = false;
    protected double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    protected double alphaAcc;  // Die Winkelbeschleunigung
    protected double alpha;     // Der Winkel
    protected double maxTurnAcc = 0.35;
    protected double shootForce = 10.0;

    public static LinkedList<Projectile> projectiles= new LinkedList<>();


    // Constructors --------------------------------
    Ship() {
        pos = new Vector(0,0,"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = 0;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        falcon = new Image("/ship.png");
    }

    Ship(Vector pos, double alpha) {
        this.pos = pos;
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

    public void turn() {
        if (isTurningL && isTurningR){
            this.alphaAcc = 0;
        } else if (isTurningL){
            this.alphaAcc = -maxTurnAcc;
        } else if (isTurningR){
            this.alphaAcc = maxTurnAcc;
        } else {
            this.alphaAcc = 0;
        }
    }

  public void shoot() {
    Projectile geschoss = new Projectile(new Vector(this.pos.getX(), this.pos.getY(), "c"),
                                         new Vector(this.shootForce, this.alpha, "p"));
    projectiles.add(geschoss);
  }

    public Vector getPos() {
        Vector position = this.pos;
        return position;
    }

    public void boost(){
        if(isBoosting){
            this.acc.setP(0.2, this.alpha);
        } else {
            this.acc.setP(0, this.alpha);
        }
    }

    public double getAbsVel() {
        double absoluteVelocity = this.vel.getLength();
        return absoluteVelocity;
    }

    public void update(){
        boost();
        turn();
        vel.add(acc);
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




    public boolean isTurningL() {
        return isTurningL;
    }

    public boolean isTurningR() {
        return isTurningR;
    }

    public boolean isBoosting() {
        return isBoosting;
    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public void setTurningL(boolean turningL) {
        isTurningL = turningL;
    }

    public void setTurningR(boolean turningR) {
        isTurningR = turningR;
    }

    public void setBoosting(boolean boosting) {
        isBoosting = boosting;
    }
}
