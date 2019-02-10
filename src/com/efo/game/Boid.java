package com.efo.game;

import com.efo.engine.Input;
import com.efo.engine.Renderer;
import com.efo.engine.Tupel;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

public class Boid extends Ship {

    //Attributes
    private Vector futureLocation, rad, desired, steer;
    private Image xWing;
    private double maxSpeed, maxForce, distance, radiusLength, futureLocationDistance = 20, radAngle = 180;
    private int[][] steeringRange = {{3}, {4, 11}, {12, 27}, {28, 43}, {44, 51}, {52}};
    private int firstRing = 10, secondRing = 0, thirdRing = 0;


    //Constructor
    public Boid(String Faction) {

        pos = new Vector((int) (Math.random() * 480), (int) (Math.random() * 320), "c");
	      oldPos.setC(pos.getX(), pos.getY());
        vel = new Vector(0, 0, "p");
        acc = new Vector(0, 0, "p");

        futureLocation = new Vector(0, 0, "p");
        rad = new Vector(0, 0, "p");

        maxSpeed = 3;
        maxForce = 0.05;
        radiusLength = 10;

        if(Faction == "empire" || Faction == "Empire" || Faction == "imperium" || Faction == "Imperium") {
            xWing = new Image("/xWing.png");
        } else if(Faction == "republic" || Faction == "Republic" || Faction == "republik" || Faction == "Republik"){
            xWing = new Image("/tieFighter.png");
        }


    }

    //Methods
    public void update(Input in, LinkedList<Boid> boids) {
        int count = 0;
        for (Boid other: boids) {
            Double d = this.pos.distance(other.pos);
            if((d > 0) && (d < 50)) {
                count++;
            }
        }
        if(count > 0) {
            flocking(boids);
        } else { floating(); }

        vel.add(acc);
        vel.limit(maxSpeed);
	      oldPos.setC(pos.getX(), pos.getY());
        pos.add(vel);

        alphaVel += alphaAcc;
        alpha = (alpha + alphaVel) % 360;

        //Velocity Drag
        vel.setP(0.962 * vel.getLength(), vel.getAngle());
        //Turning Drag
        alphaVel *= 0.000855;

        acc.mult(0.0);
    }

    @Override
    public void show(Renderer r) {
        r.drawImage(xWing, (int) this.pos.getX(), (int) this.pos.getY(), Math.toRadians(vel.getAngle()));
    }

    public void flocking(LinkedList<Boid> boids) {
        Vector sep = seperate(boids);
        Vector coh = cohesion(boids);
        Vector ali = align(boids);

        floating();

        //sep.mult(1.5);
        //coh.mult(1.0);
        ali.mult(5.0);

        //applyForce(sep);
        //applyForce(coh);
        applyForce(ali);
    }

    public Vector seperate(LinkedList<Boid> boids) {
        Double desiredSeperation = 60.0;
        Vector steer = new Vector(0,0, "p");
        Double count = 0.0;

        for (Boid other: boids) {
            Double d = this.pos.distance(other.pos);
            if((d > 0) && (d < desiredSeperation)) {
                Vector diff = this.pos.sub(other.pos,true);
                diff.setLength(1);
                diff.div(d);
                steer.add(diff);
                count++;
            }
        }

        if(count > 0) {
            steer.div(count);
        }

        if(steer.getLength() > 0) {
            steer.setLength(1);
            steer.mult(maxSpeed);
            steer.sub(vel);
            steer.limit(maxForce);
        }
        return steer;
    }

    public Vector align(LinkedList<Boid> boids) {
        float neighborDistance = 50;
        Vector sum = new Vector(0,0, "p");
        Double count = 0.0;
        for (Boid other: boids) {
            Double d = this.pos.distance(other.pos);
            if((d > 0) && (d < neighborDistance)) {
                sum.add(other.vel);
                count++;
            }
        }
        if(count > 0) {
            sum.div(count);
            sum.setLength(1);
            sum.mult(maxSpeed);
            Vector steer = sum.sub(vel,true);
            steer.limit(maxForce);
            return steer;
        } else {
            return new Vector(0,0,"p");
        }
    }

    public boolean peripheralVision(LinkedList<Boid> boids) {
        //Vector sum = new Vector(0,0,"p");
        Double count = 0.0;

        /*System.out.println("offx: " + (int)offSetFront.getX() + " offy: " + (int)offSetFront.getY());
        System.out.println("x: " + (int)this.pos.getX() + " y: " + (int)this.pos.getY());
        System.out.println((int)this.vel.getAngle());*/

        for (Boid other:boids) {
            Vector diff = other.pos.sub(this.pos,true);
            boolean isTargetFront = (Math.abs(diff.getAngle() - this.vel.getAngle()) + 360) % 360 < 30;
            if((diff.getLength() > 0) && (diff.getLength() < 50) && isTargetFront) {
                //sum.add(other.vel);
                count++;
            }
        }
        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Vector cohesion(LinkedList<Boid> boids) {
        float neighborDistance = 80;
        Vector sum = new Vector(0,0,"p");
        Double count = 0.0;

        for (Boid other: boids) {
            Double d = this.pos.distance(other.pos);
            if((d > 0) && (d < neighborDistance)) {
                sum.add(other.pos);
                count++;
            }
        }
        if(count > 0) {
            sum.div(count);
            return seek(sum);
        }
        else {
            return new Vector(0,0,"p");
        }
    }

    public void floating() {

        futureLocation.setP(futureLocationDistance, vel.getAngle());
        int rand = (int) (Math.random() * 56);

        if (rand <= steeringRange[0][0]) {
            radAngle -= firstRing;
        } else if (rand > steeringRange[1][0] && rand < steeringRange[1][1]) {
            radAngle -= secondRing;
        } else if (rand > steeringRange[2][0] && rand < steeringRange[2][1]) {
            radAngle -= thirdRing;
        } else if (rand > steeringRange[3][0] && rand < steeringRange[3][1]) {
            radAngle += thirdRing;
        } else if (rand > steeringRange[4][0] && rand < steeringRange[4][1]) {
            radAngle += secondRing;
        } else if (rand > steeringRange[5][0]) {
            radAngle += firstRing;
        }


        rad.setP(radiusLength, radAngle);

        desired = (futureLocation.add(rad, true));
        desired.mult(0.005);
        desired.setLength(1);
        desired.mult(maxSpeed);

        steer = desired.sub(vel, true);

        steer.limit(maxForce);

        applyForce(steer);
    }



    public Vector seek(Vector target) {

        Vector desired = (new Vector(target.getX(), target.getY(), "c")).sub(pos, true);
        desired.setLength(1);
        desired.mult(maxSpeed);

        Vector steer = desired.sub(vel, true);

        steer.limit(maxForce);

        return steer;
    }

    public void flee(Vector target) {
        if (distance < 80) {
            desired = (pos).sub(new Vector(target.getX(), target.getY(), "c"), true);

            desired.mult(2.0);
            desired.setLength(1);
            desired.mult(maxSpeed);

            steer = desired.sub(vel, true);

            steer.limit(maxForce);

            acc.setP(steer.getLength(), steer.getAngle());
        }
    }

    @Override
    public void shoot() {
        projectiles.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10,vel.getAngle(),"p"),true)),
                new Vector(this.shootForce, vel.getAngle(), "p")));
    }

    public void applyForce(Vector force) {
        acc.add(force);
    }
}
