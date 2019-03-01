package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Self Made

public class Doll {
    LinkedList<Image> images;
    LinkedList<Vector> posVel;
    Double angle;
    Double angleVel;
    int spin;


    Doll(Ship ship, String model) {
        images = new LinkedList<>();
        posVel = new LinkedList<>();
        int spread = 5;

        if (model.equals("falcon")) {
            images.add(new Image("/ships/falcon.png"));
            images.add(new Image("/ships/falcon.png"));
            posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
            posVel.add(new Vector(ship.getVel().getLength(),ship.getPos().getAngle(),"p"));
            posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
            posVel.add(new Vector(ship.getVel().getLength(),ship.getPos().getAngle(),"p"));
            this.angle = (ship.getAlpha() + 360) %360;
            this.angleVel = ship.getAlphaVel();

        } else if (model.equals("interceptor")) {
            images.add(new Image("/ships/falcon.png"));
            images.add(new Image("/ships/falcon.png"));
            posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
            posVel.add(new Vector(ship.getVel().getLength(),ship.getPos().getAngle(),"p"));
            posVel.add(new Vector(ship.getPos().getX(),ship.getPos().getY(),"c"));
            posVel.add(new Vector(ship.getVel().getLength(),ship.getPos().getAngle(),"p"));
            this.angle = (ship.getAlpha() + 360) %360;
            this.angleVel = ship.getAlphaVel();

        }
        posVel.get(1).setAngle(posVel.get(1).getAngle()+spread);
        posVel.get(3).setAngle(posVel.get(3).getAngle()-spread);
    }

    Doll(Boid boid, String model) {
        images = new LinkedList<>();
        posVel = new LinkedList<>();
        int spread = 5;

        if (model.equals("xWing")) {
            images.add(new Image("/dollModel/xWing1"));
            images.add(new Image("/dollModel/xWing2"));
            posVel.add(new Vector(boid.getPos().getX(),boid.getPos().getY(),"c"));
            posVel.add(new Vector(boid.getVel().getLength(),boid.getPos().getAngle(),"p"));
            posVel.add(new Vector(boid.getPos().getX(),boid.getPos().getY(),"c"));
            posVel.add(new Vector(boid.getVel().getLength(),boid.getPos().getAngle(),"p"));
        } else if (model.equals("tieFighter")) {
            images.add(new Image("/dollModel/tieFighter1"));
            images.add(new Image("/dollModel/tieFighter2"));
            posVel.add(new Vector(boid.getPos().getX(),boid.getPos().getY(),"c"));
            posVel.add(new Vector(boid.getVel().getLength(),boid.getPos().getAngle(),"p"));
            posVel.add(new Vector(boid.getPos().getX(),boid.getPos().getY(),"c"));
            posVel.add(new Vector(boid.getVel().getLength(),boid.getPos().getAngle(),"p"));
        }
        posVel.get(1).setAngle(posVel.get(1).getAngle()+spread);
        posVel.get(3).setAngle(posVel.get(3).getAngle()-spread);
    }

    //duality could've been handled with inheritence
    void updateSing() {
        System.out.println(posVel.get(1).getLength());
        posVel.get(0).add(posVel.get(1));
        posVel.get(0).mult(0.998);
        angle = (angle + angleVel) % 360;
        //System.out.println("angle: " + angle + " aVel: " + angleVel);
    }



    void updateMult() {
        posVel.get(0).add(posVel.get(1));
        posVel.get(2).add(posVel.get(3));
    }

    void showSing(Renderer r) {
            r.drawImage(images.get(0),(int)posVel.get(0).getX(),(int)posVel.get(0).getY(),angle);
        System.out.println(angle);
    }

    /*void showMult(Renderer r) {
            r.drawImage(images.get(0),(int)posVel.get(0).getX(),(int)posVel.get(0).getY(),angle);
            r.drawImage(images.get(1),(int)posVel.get(2).getX(),(int)posVel.get(2).getY(),Math.toRadians(posVel.get(3).getAngle()));
    }*/





    public boolean border() {
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
