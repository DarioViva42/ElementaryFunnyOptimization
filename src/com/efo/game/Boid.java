package com.efo.game;

import com.efo.engine.Input;
import com.efo.engine.Renderer;
import com.efo.engine.Vector;

public class Boid extends Ship {

    //Attributes
    Vector steering, desired;
    double maxSpeed;

    //Constructor
    public Boid() {
        steering = new Vector(0,0,"p");
        desired = new Vector(0,0,"p");
        vel = new Vector(0,0,"p");
    }

    //Methods
    public void update(Input in, Ship uss) {
        desired = (new Vector(uss.getX(),uss.getY(),"c")).sub(pos,true);

        desired.setLength(1);
        desired.mult(1.5);

        steering = desired.sub(vel,true);

        //System.out.println("Steering: " + steering.getLength() + " Desired: " + desired.getLength());

        vel.add(steering);
        pos.add(vel);
        alphaVel += alphaAcc;
        alpha = (alpha + alphaVel) % 360;

        //drag
        double l = this.vel.getLength();
        double a = this.vel.getAngle();
        //Velocity Drag
        this.vel.setP(0.992 * l, a);
        //Turning Drag
        this.alphaVel *= 0.9855;

    }

    @Override
    public void show(Renderer r) {
        r.drawImage(enterprise, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha + 90));
    }


}
