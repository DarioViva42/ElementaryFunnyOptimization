//Our intellectual property

package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;

public class Star {

    // Attributes ---------------------------------
    private Vector pos;
    private Vector vel;

    // Constructor --------------------------------
    Star() {
        double sAngle = 140;
        double sVel = 0.01;

        int width = 480, height = 320;
        double sX = random(0,width);
        double sY = random(0,height);
        pos = new Vector(sX,sY,"c");

        this.vel = new Vector(sVel,sAngle,"p");
        //newSizeVel();
    }


    // Methods ------------------------------------
    private double random(double a, double b){
        return Math.random()*(b-a) + a;
    }

    void show(Renderer r , int width, int height) {
        r.setPixel((int)(pos.getX()),(int)(pos.getY()),0xfffffde5);

        //wenn rechts raus
        if(this.pos.getX() > width) {
            this.pos.setC(0,random(0,height));
            //newSizeVel();

            //wenn links raus
        } else if(this.pos.getX() < 0) {
            this.pos.setC(width,random(0,height));
            //newSizeVel();

            //wenn unten raus
        } else if(this.pos.getY() > height) {
            this.pos.setC(random(0,width),0);
            //newSizeVel();

            //wenn oben raus
        } else if(this.pos.getY() < 0){
            this.pos.setC(random(0,width),height);
            //newSizeVel();
        }
    }

    public void update() {
        this.pos.add(this.vel);
    }

}



