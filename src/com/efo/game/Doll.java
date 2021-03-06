//Our intellectual property

package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Self Made
class Doll {
    private LinkedList<Image> images;
    private LinkedList<Vector> posVel;
    private Double angleFirst, angleSecond;
    private Double angleVelFirst, angleVelSecond;
    private Explosion explosion = new Explosion(11,5.0);


    Doll(Ship ship, String model) {
        images = new LinkedList<>();
        posVel = new LinkedList<>();
        int spread = 2;

        if (model.equals("falcon")) {
            images.add(new Image("/dollModel/falconWrack1.png"));
            images.add(new Image("/dollModel/falconWrack2.png"));
        } else if(model.equals("interceptor")) {
            images.add(new Image("/dollModel/interceptorWrack2.png"));
            images.add(new Image("/dollModel/interceptorWrack1.png"));
        }

        posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
        posVel.add(new Vector(ship.getVel().getLength(),ship.getVel().getAngle() + spread,"p"));
        posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
        posVel.add(new Vector(ship.getVel().getLength(),ship.getVel().getAngle() - spread,"p"));
        this.angleFirst = (ship.getAlpha() + 360) % 360;
        this.angleSecond = (ship.getAlpha() + 360) % 360;
        this.angleVelFirst = ship.getAlphaVel();
        this.angleVelSecond = ship.getAlphaVel();
    }

    Doll(Boid boid, String model) {

        images = new LinkedList<>();
        posVel = new LinkedList<>();
        int spread = 2;

        if (model.equals("xWing")) {
            images.add(new Image("/dollModel/xWingWrack1.png"));
            images.add(new Image("/dollModel/xWingWrack2.png"));
        } else if (model.equals("tieFighter")) {
            images.add(new Image("/dollModel/tieFighterWrack2.png"));
            images.add(new Image("/dollModel/tieFighterWrack1.png"));
        }
        posVel.add(new Vector(boid.getPos().getX(), boid.getPos().getY(), "c"));
        posVel.add(new Vector(boid.getVel().getLength(), boid.getVel().getAngle() + spread, "p"));
        posVel.add(new Vector(boid.getPos().getX(), boid.getPos().getY(), "c"));
        posVel.add(new Vector(boid.getVel().getLength(), boid.getVel().getAngle() - spread, "p"));
        angleFirst = (boid.vel.getAngle() + 360) % 360;
        angleSecond = (boid.vel.getAngle() + 360) % 360;
        angleVelFirst = Math.random();
        angleVelSecond = -(Math.random());
    }

    void update() {
        Double dragVel = 0.995, dragAngle = 0.99995;

        posVel.get(0).add(posVel.get(1));
        posVel.get(1).mult(dragVel);
        angleFirst = (angleFirst + angleVelFirst) % 360;
        angleVelFirst *= dragAngle;
        posVel.get(2).add(posVel.get(3));
        posVel.get(3).mult(dragVel);
        angleSecond = (angleFirst + angleVelSecond) % 360;
        angleVelSecond *= dragAngle;
        if(explosion != null) explosion.update();
        if(explosion != null && explosion.isFinished()) explosion = null;
    }

    void show(Renderer r) {
        r.drawImage(images.get(0),(int)posVel.get(0).getX(),(int)posVel.get(0).getY(),Math.toRadians(angleFirst));
        r.drawImage(images.get(1),(int)posVel.get(2).getX(),(int)posVel.get(2).getY(),Math.toRadians(angleSecond));
        if(explosion != null) explosion.show(r,posVel.get(0));
    }

    void border() {
        //If out of border left -> go in from the right
        if(posVel.get(0).getX() < -20) {
            posVel.get(0).setC(480 + 20,posVel.get(0).getY());
            //If out of border to the right -> go int from the left
        } else if(this.posVel.get(0).getX() > (480 + 20)) {
            posVel.get(0).setC(-20,posVel.get(0).getY());
            //If out of border at the top -> go in from the bottom
        } else if(posVel.get(0).getY() < -20) {
            posVel.get(0).setC(posVel.get(0).getX(),320 +  20);
            //If out of border at the bottom -> go in from the top
        }else if(posVel.get(0).getY() > (320 + 20)) {
            posVel.get(0).setC(posVel.get(0).getX(), -20);
        }
        if(posVel.get(2).getX() < -20) {
            posVel.get(2).setC(480 + 20,posVel.get(2).getY());
            //If out of border to the right -> go int from the left
        } else if(this.posVel.get(2).getX() > (480 + 20)) {
            posVel.get(2).setC(-20,posVel.get(2).getY());
            //If out of border at the top -> go in from the bottom
        } else if(posVel.get(0).getY() < -20) {
            posVel.get(2).setC(posVel.get(2).getX(),320 +  20);
            //If out of border at the bottom -> go in from the top
        }else if(posVel.get(2).getY() > (320 + 20)) {
            posVel.get(2).setC(posVel.get(2).getX(), -20);
        }
    }

    boolean isBorder() {
        //If out of border left -> go in from the right
        if(posVel.get(0).getX() < -20) {
            return true;
            //If out of border to the right -> go int from the left
        } else if(posVel.get(0).getX() > (480 + 20)) {
            return true;
            //If out of border at the top -> go in from the bottom
        } else if(posVel.get(0).getY() < -20) {
            return true;
            //If out of border at the bottom -> go in from the top
        }else if(posVel.get(0).getY() > (320 + 20)) {
            return true;
        } else {
            return false;
        }
    }
}
