package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;


import java.util.LinkedList;

//Selfmade
public class Ship extends Vehicle{

    protected double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    protected double alphaAcc;  // Die Winkelbeschleunigung
    protected double alpha;     // Der Winkel
    public String playerName;
    private int HP = 100;
    boolean alive = true;
    LinkedList<Vector> exPos = new LinkedList<>();
		Double shotCap = 0.0, attackSpeed = 1.0/15.0;
    int size = 15;


    // Constructors --------------------------------

    Ship(Vector pos, double alpha, String name, String faction) {

        this.faction = faction;
        playerName = name;

        this.pos = pos;
        oldPos = new Vector(getX(),getY(),"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        model = new Image("/falcon.png");

    }

    public void update(){
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
                explosions.remove(i);
            }
        }
    }

    public void turn(double turn) {
        this.alphaAcc = turn;
    }

    public void show(Renderer r){
        r.drawImage(model, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.alpha));
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).show(r, this.pos.add(exPos.get(i),true));
        }
    }

    public void setBoost(double boost) {
        this.acc.setP(boost, this.alpha);
    }

    public void shoot(boolean sound) {

        if(this.faction.equals("rebel")) {
	        if(shotCap >= 1) {
		        Vehicle.rebelLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
				        new Vector(this.shootForce, alpha, "p")));
		        if(sound){
			        sounds.get(5).play();
		        }

		        shotCap = 0.0;
	        }
        } else {
	        if(shotCap >= 1) {
		        Vehicle.empireLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
				        new Vector(this.shootForce, alpha, "p")));
		        if(sound){
			        sounds.get(5).play();
		        }
		        shotCap = 0.0;
	        }
        }
    }

    public boolean hit(boolean sound) {
        if (faction.equals("rebel")) {
            for (int i = 0; i < empireLasers.size();i++) {
                Double d = this.pos.distance(empireLasers.get(i).getPos());
                if(d < size) {
                    HP--;
                    if(sound){
	                    sounds.get(0).play();
                    }
                    exPos.add(new Vector(Math.random()*10,Math.random()*360,"p"));
                    explosions.add(new Explosion(11,5.0));
                    empireLasers.remove(i);
                }
            }

            if(HP <= 0) {
                return true;
            } else {
                return false;
            }

        } else if (faction.equals("empire")) {
            for (int i = 0; i < rebelLasers.size();i++) {
                Double d = this.pos.distance(rebelLasers.get(i).getPos());
                if(d < size) {
                    HP--;
                    if(sound){
	                    sounds.get(0).play();
                    }
                    exPos.add(new Vector(Math.random()*13,Math.random()*360,"p"));
                    explosions.add(new Explosion(11,5.0));
                    rebelLasers.remove(i);
                }
            }

            if(HP <= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int getHP() {
        return HP;
    }
}
