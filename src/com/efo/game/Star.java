package com.efo.game;

import com.efo.engine.Engine;
import com.efo.engine.Vector;

public class Star {

        // Attributes ---------------------------------
        Vector pos;
        Vector vel;
        double starSize;

        //random speed
        double sVel;
        //static angle for the Stars
        double sAngle = 140;

        //Star size
        double stSiLower = 1.5;
        double stSiUpper = 1.8;
        double stSiPow = 10;
        double stSiModeration = 120;

        //starSize to velocity Mapping
        double lowerStarSize = 0.5;
        double upperStarSize = 3;
        double lowerVel = 0;
        double upperVel = 0.5;

        // Constructor --------------------------------
        Star(Engine ge) {
            double sX = Math.random()*ge.getWidth();
            double sY = Math.random()*ge.getHeight();
            this.pos = new Vector(sX,sY,"c");

            newSizeVel();
        }

        Star(int direction) {
            double sX;
            double sY;
            switch(direction) {
                case 1:

                    sX = 0;
                    sY = random(0,height);

                    this.pos = new Vector(sX,sY,"c");
                    newSizeVel();
                    break;

                case 2:

                    sX = random(0,width);
                    sY = 0;

                    this.pos = new Vector(sX,sY,"c");
                    newSizeVel();
                    break;

                case 3:

                    sX = width;
                    sY = random(0,height);

                    this.pos = new Vector(sX,sY,"c");
                    newSizeVel();
                    break;

                case 4:

                    sX = random(0,width);
                    sY = height;

                    this.pos = new Vector(sX,sY,"c");
                    newSizeVel();
                    break;

                default:
                    System.out.println("Problem with the new Stars");
            }
        }


        // Methods ------------------------------------
        void show() {
            fill(#fffeed);
            noStroke();
            ellipse(this.pos.getX(),this.pos.getY(),starSize,starSize);

            //wenn rechts raus
            if(this.pos.getX() > width) {
                this.pos.setC(0,random(0,height));
                newSizeVel();

                //wenn links raus
            } else if(this.pos.getX() < 0) {
                this.pos.setC(width,random(0,height));
                newSizeVel();

                //wenn unten raus
            } else if(this.pos.getY() > height) {
                this.pos.setC(random(0,width),0);
                newSizeVel();

                //wenn oben raus
            } else if(this.pos.getY() < 0){
                this.pos.setC(random(0,width),height);
                newSizeVel();
            }
        }

        void update() {
            this.pos.add(this.vel);
        }

        void newSizeVel() {
            // starSizeModeration für Darios bildschirm halbieren!(60), für Luis(120)
            this.starSize = (pow(random(stSiLower,stSiUpper),stSiPow))/stSiModeration;

            //speed mapped to size of Star
            this.sVel = map(starSize,lowerStarSize,upperStarSize,lowerVel,upperVel);
            this.vel = new Vector(sVel,sAngle,"p");
        }

}



