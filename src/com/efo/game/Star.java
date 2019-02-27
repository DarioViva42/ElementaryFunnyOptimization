package com.efo.game;

import com.efo.engine.Engine;
import com.efo.engine.Renderer;
import com.efo.engine.Vector;

//Selfmade

public class Star {

    // Attributes ---------------------------------
    private Vector pos;
    private Vector vel;
    private double starSize;

    //Math.random speed
    private double sVel;
    //static angle for the Stars
    private double sAngle = 140;

    //Star size
    private double stSiLower = 1.5;
    private double stSiUpper = 1.8;
    private double stSiPow = 10;
    private double stSiModeration = 120;

    //starSize to velocity Mapping
    private double lowerStarSize = 0.5;
    private double upperStarSize = 3;
    private double lowerVel = 0;
    private double upperVel = 0.5;


    private int width = 480, height = 320;

    // Constructor --------------------------------
    public Star() {
        double sX = random(0,width);
        double sY = random(0,height);
        pos = new Vector(sX,sY,"c");

        sVel = 0.01;
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


    public void newSizeVel() {
        // starSizeModeration für Darios bildschirm halbieren!(60), für Luis(120)
        this.starSize = (Math.pow((Math.random() * ( stSiUpper - stSiLower )) + stSiLower,stSiPow))/stSiModeration;

        //speed mapped to size of Star
        //this.sVel = map(starSize,lowerStarSize,upperStarSize,lowerVel,upperVel);
        this.vel = new Vector(sVel,sAngle,"p");
    }

}



