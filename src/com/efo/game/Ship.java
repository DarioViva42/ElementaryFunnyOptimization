package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

//Selfmade
public class Ship {
    // Attributes ----------------------------------
    Vector pos;   // Position
    Vector gunPos;// Position der Waffe (Vorne)
    Vector vel;   // Geschwidigkeit
    Vector acc;   // Beschleunigung
    Image schiff;

    boolean isTurningL = false;
    boolean isTurningR = false;
    boolean isBoosting = false;
    double alphaVel;  // Die Winkelgeschwindigkeit des Schiffes
    double alphaAcc;  // Die Winkelbeschleunigung
    double alpha;     // Der Winkel
    double maxTurnAcc = 0.2;


    //Ship build up
    int head = 20;
    int wings = 10;

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
      r.drawImage(schiff, (int)this.pos.getX(), (int)this.pos.getY(), this.alpha);
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
            this.acc.setP(0.1, this.alpha);
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
        System.out.println(a);
        //Velocity Drag
        this.vel.setP(0.992 * l, a);
        //Turning Drag
        this.alphaVel *= 0.9855;
    }
}
