package com.efo.game;

import com.efo.engine.Input;
import com.efo.engine.Renderer;
import com.efo.engine.Tupel;
import com.efo.engine.Vector;

import java.util.LinkedList;

public class Boid extends Ship {

    //Attributes
    Vector steer, desired, futureLocation, rad;
    double maxSpeed = 3, maxForce = 0.05, distance, radiusLength = 10, futureLocationDistance = 20,radAngle = 180;
    int[][] steeringRange = {{3},{4,11},{12,27},{28,43},{44,51},{52}};
    int firstRing = 15, secondRing = 10, thirdRing = 5;


    //Constructor
    public Boid() {

        pos = new Vector ((int)(Math.random()* 480),(int)(Math.random()* 320),"c");
        steer = new Vector(0,0,"p");
        desired = new Vector(0,0,"p");
        vel = new Vector(0,90,"p");
        acc = new Vector(0,0,"p");
        futureLocation = new Vector(0,0,"p");
        rad = new Vector(0,0,"p");

    }

    //Methods
    public void update(Input in, Vector target, LinkedList<Boid> boids) {
        distance = pos.distance(target);

            seperate(boids);
            //seek(target);
            //floating();


        //System.out.println("steer: " + steer.getLength() + " Desired: " + desired.getLength());

        vel.add(acc);
        pos.add(vel);
        alphaVel += alphaAcc;
        alpha = (alpha + alphaVel) % 360;

        //Velocity Drag
        vel.setP(0.992 * vel.getLength(), vel.getAngle());
        //Turning Drag
        alphaVel *= 0.9855;

    }

    @Override
    public void show(Renderer r) {
        r.drawImage(enterprise, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(vel.getAngle() + 90));
    }

    public void floating() {

        futureLocation.setP(futureLocationDistance, vel.getAngle());
        int rand = (int)(Math.random() * 56);

        if(rand <= steeringRange[0][0]) {
            radAngle -= firstRing;
        } else if(rand > steeringRange[1][0] && rand < steeringRange[1][1]) {
            radAngle -= secondRing;
        } else if(rand > steeringRange[2][0] && rand < steeringRange[2][1]) {
            radAngle -= thirdRing;
        } else if(rand > steeringRange[3][0] && rand < steeringRange[3][1]) {
            radAngle += thirdRing;
        } else if(rand > steeringRange[4][0] && rand < steeringRange[4][1]) {
            radAngle += secondRing;
        } else if(rand > steeringRange[5][0]) {
            radAngle += firstRing;
        }


        rad.setP(radiusLength,radAngle);

        desired = (futureLocation.add(rad,true));
        desired.mult(0.005);
        desired.setLength(1);
        desired.mult(maxSpeed);

        steer = desired.sub(vel,true);

        steer.limit(maxForce);

        acc.setP(steer.getLength(),steer.getAngle());
    }

    public void flocking(LinkedList<Boid> boids) {
        /*Vector sep = seperate(boids);
        Vector ali = align(boids);
        Vector coh = cohesion(boids);

        sep.mult(1.5);
        ali.mult(1.0);
        coh.mult(1.0);

        */
    }

    public void seek(Vector target) {

        //desired = target - location
        desired = (new Vector(target.getX(),target.getY(),"c")).sub(pos,true);
        desired.mult(0.005);
        desired.setLength(1);
        desired.mult(maxSpeed);
        
        steer = desired.sub(vel,true);

        steer.limit(maxForce);

        acc.setP(steer.getLength(),steer.getAngle());
    }

    public void flee(Vector target) {
        if(distance < 80) {
            desired = (pos).sub(new Vector(target.getX(), target.getY(), "c"), true);

            desired.mult(2.0);
            desired.setLength(1);
            desired.mult(maxSpeed);

            steer = desired.sub(vel, true);

            steer.limit(maxForce);

            acc.setP(steer.getLength(), steer.getAngle());
        }
    }

    public void seperate(LinkedList<Boid> boids) {
        int desiredSeperation = 200;

        for(Boid other: boids) {
            Double d = pos.distance(other.pos);

            if((d > 0) && (d < desiredSeperation)) {
                Vector diff = pos.sub(other.pos,true);

                diff.setLength(1);
            }
        }
    }
}
