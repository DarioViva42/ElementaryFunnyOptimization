package com.efo.game;

import com.efo.engine.Vector;

public class Meteor {
    class Meteor {

        // Attributes ---------------------------------
        Vector pos;
        Vector vel;

        int children = 2;
        double spread = 90;

        int large = 50;
        int medium = 35;
        int small = 20;

        int position;
        double angle;
        int size;

        // Constructor ---------------------------------
        Meteor(int size) {
            this.size = size;
            //one = small / two = medium / three = large
            this.position = (int)random(0,4);

            System.out.println("Random Size Meteor: " + position);

            switch(this.position) {

                //rechts
                case 0:
                    this.pos = new Vector(random(width-20,width+10),random(-10,height+10),"c");
                    this.angle = random(120,240);
                    break;

                //unten
                case 1:
                    this.pos = new Vector(random(-10,width+10),random(height-20,height+20),"c");
                    this.angle = random(210,330);
                    break;

                //links
                case 2:
                    this.pos = new Vector(random(-10,20),random(-10,height+10),"c");
                    int test1 = (int)random(0,1);
                    if(test1 == 0) {
                        this.angle = random(300,360);
                    } else {
                        this.angle = random(0,60);
                    }

                    break;

                //oben
                case 3:
                    this.pos = new Vector(random(-10,width+10),random(-10,20),"c");
                    this.angle = random(30,150);
                    break;

                default:
                    System.out.println("RIP");
                    noLoop();
            }

            this.vel = new Vector(random(1,2),this.angle,"p");

            int test = (int)random(0,3);

            switch(test) {
                case 0:
                    this.size = this.small;
                    break;

                case 1:
                    this.size = this.medium;
                    break;

                case 2:
                    this.size = this.large;
                    break;

                default:
                    System.out.println("Meteors need to have a size between 1 and 3");
                    // Programm beenden
            }

        }

        // Methods -------------------------------------
        private double rendom(double a, double b){
            return Math.random()*(b-a) + a;
        }

        void show() {
            // Bild von Meteorit.
        }

        void update() {
            this.pos.add(this.vel);
        }

        Meteor[] shot() {
            if (size == 1){
                return null;
            }
            Meteor[] children = new Meteor[this.children];
            return children;
        }

        Vector getPos() {
            Vector position = this.pos;
            return position;
        }

        int getSize() {
            int size = this.size;
            return size;
        }
    }

}
