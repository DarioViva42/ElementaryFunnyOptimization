package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;


import java.util.LinkedList;

//Selfmade
public class Ship extends Vehicle{

    private double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    private double alphaAcc;  // Die Winkelbeschleunigung
    private double alpha;     // Der Winkel

    String playerName;
    private int HP = 30;
    boolean alive = true, gun = true;
    private LinkedList<Vector> exPos = new LinkedList<>();
		private double shotCap = 0.0;


    // Constructors --------------------------------

    Ship(Vector pos, double alpha, String name, String faction) {

        this.faction = faction;
        playerName = name;

        this.pos = pos;
        oldPos = new Vector(getX(),getY(),"c");

        alphaVel = 0;
        alphaAcc = 0;
        this.alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        if(faction.equals("empire")) {
            model = new Image("/ships/interceptor.png");
        } else if(faction.equals("rebel")) {
            model = new Image("/ships/falcon.png");
        }


    }

    public void update(){
        double attackSpeed = 1.0/15.0;

        //System.out.println(this.HP);

        vel.add(acc);
        oldPos.setC(pos.getX(), pos.getY());
        pos.add(vel);
        alphaVel += alphaAcc;
        alpha = (alpha + alphaVel) % 360;

        //drag
        double l = this.vel.getLength();
        double a = this.vel.getAngle();

        //Velocity Drag
        this.vel.setP(0.98 * l, a);

        //Turning Drag
        this.alphaVel *= 0.937;

        if(shotCap<=1) {
	        shotCap += attackSpeed;
        }

        for (Explosion ex: explosions) {
            ex.update();
        }

        for (int i = 0; i < explosions.size(); i++) {
            if(explosions.get(i).isFinished()) {
                // Wenn die Funktion fertig ist, soll Sie gelöscht werden.
                explosions.remove(i);
            }
        }
    }

    void turn(double turn) {
        this.alphaAcc = turn;
    }

    public void show(Renderer r){
        r.drawImage(model, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha));
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).show(r, this.pos.add(exPos.get(i),true));
        }
    }

    void setBoost(double boost) {
        this.acc.setP(boost, this.alpha);
    }

    void shoot(boolean sound) {

        if(this.faction.equals("rebel")) {
	        if(shotCap >= 1) {
	            if(gun) {
                    Vehicle.rebelLasers.add(new Projectile(pos.add(new Vector(8,alpha-30,"p"),true),new Vector(this.shootForce, alpha, "p")));
                    if (sound) {
                        sounds.get(5).play();
                    }

                    shotCap = 0.0;
                    gun = false;
                } else {
                    Vehicle.rebelLasers.add(new Projectile(pos.add(new Vector(7,alpha+30,"p"),true),new Vector(this.shootForce, alpha, "p")));
                    if (sound) {
                        sounds.get(5).play();
                    }

                    shotCap = 0.0;
                    gun = true;
                }
	        }
        } else if(this.faction.equals("empire")) {
	        if(shotCap >= 1) {
	            if(gun) {
                    Vehicle.empireLasers.add(new Projectile(pos.add(new Vector(8,alpha-20,"p"),true),new Vector(this.shootForce, alpha, "p")));
                    if (sound) {
                        sounds.get(5).play();
                    }
                    shotCap = 0.0;
                    gun = false;
                } else {
                    Vehicle.empireLasers.add(new Projectile(pos.add(new Vector(8,alpha+20,"p"),true),new Vector(this.shootForce, alpha, "p")));
                    if (sound) {
                        sounds.get(5).play();
                    }
                    shotCap = 0.0;
                    gun = true;
                }
	        }
        }
    }

    boolean hit(boolean sound) {
        int size = 15;

        if (faction.equals("rebel")) {
            for (int i = 0; i < empireLasers.size();i++) {
                double d = this.pos.distance(empireLasers.get(i).getPos());
                if(d < size) {
                    HP--;
                    if(sound){
	                    sounds.get(0).play();
                    }
                    exPos.add(new Vector(Math.random()*10,Math.random()*360,"p"));
                    explosions.add(new Explosion(11,5.0));
                    // Wenn ein Laser ein Schiff getroffen hat, soll er gelöscht werden.
                    empireLasers.remove(i);
                }
            }

            return (HP <= 0);

        } else if (faction.equals("empire")) {
            for (int i = 0; i < rebelLasers.size();i++) {
                double d = this.pos.distance(rebelLasers.get(i).getPos());
                if(d < size) {
                    HP--;
                    if(sound){
	                    sounds.get(0).play();
                    }
                    exPos.add(new Vector(Math.random()*13,Math.random()*360,"p"));
                    explosions.add(new Explosion(11,5.0));
                    // Wenn ein Laser ein Schiff getroffen hat, soll er gelöscht werden.
                    rebelLasers.remove(i);
                }
            }

            return (HP <= 0);
        } else {
            return false;
        }
    }

    int getHP() {
        return HP;
    }
}
