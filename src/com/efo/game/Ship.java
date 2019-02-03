package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

//Selfmade
public class Ship {
    // Attributes ----------------------------------
    private Vector pos;   // Position
    private Vector gunPos;// Position der Waffe (Vorne)
    private Vector vel;   // Geschwidigkeit
    private Vector acc;   // Beschleunigung
    private Image schiff;

    private boolean isTurningL = false;
    private boolean isTurningR = false;
    private boolean isBoosting = false;
    private double alphaVel;  // Die Winkelgeschwindigkeit des Schiffes
    private double alphaAcc;  // Die Winkelbeschleunigung
    private double alpha;     // Der Winkel
    private double maxTurnAcc = 0.16;


    // Constructor ---------------------------------
    Ship(Vector pos, double alpha) {
        this.pos = pos;
        this.alphaVel = 0;
        this.alphaAcc = 0;
        this.alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        schiff = new Image("/ship.png");
    }

    // Methods -------------------------------------
    void show(Renderer r){
      r.drawImage(schiff, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha + 90));
    }

    void turn() {
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

  /*Projectile shoot() {
    Projectile geschoss = new Projectile(this.gunPos, this.alpha);
    return geschoss;
  }*/

    Vector getPos() {
        Vector position = this.pos;
        return position;
    }

    void boost(){
        if(isBoosting){
            this.acc.setP(0.02, this.alpha);
        } else {
            this.acc.setP(0, this.alpha);
        }
    }

    double getAbsVel() {
        double absoluteVelocity = this.vel.getLength();
        return absoluteVelocity;
    }

    void update(){
        boost();
        turn();
        this.vel.add(this.acc);
        this.pos.add(this.vel);
        this.alphaVel += alphaAcc;
        this.alpha = (this.alpha + alphaVel) % 360;

        //drag
        double l = this.vel.getLength();
        double a = this.vel.getAngle();
        //Velocity Drag
        this.vel.setP(0.992 * l, a);
        //Turning Drag
        this.alphaVel *= 0.9855;
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
