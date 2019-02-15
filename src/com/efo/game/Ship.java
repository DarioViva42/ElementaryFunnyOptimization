package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

//Selfmade
public class Ship extends Vehicle{

    protected double alphaVel;  // Die Winkelgeschwindigkeit des enterprisees
    protected double alphaAcc;  // Die Winkelbeschleunigung
    protected double alpha;     // Der Winkel
    public String playerName;
    private int HP;
    boolean alive = true;

    private SoundClip clip;




    // Constructors --------------------------------
    Ship(String name) {

        clip = new SoundClip("/audio/explosion.wav");
        clip.setVolume(-20);
        faction = "empire";
        playerName = name;

        pos = new Vector(0,0,"c");
        oldPos = new Vector(0,0,"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = 0;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        model = new Image("/ship.png");
        HP = 10;

    }

    Ship(Vector pos, double alpha, String name) {

        clip = new SoundClip("/audio/explosion.wav");
        clip.setVolume(-20);
        faction = "empire";
        playerName = name;

        this.pos = pos;
        oldPos = new Vector(getX(),getY(),"c");
        alphaVel = 0;
        alphaAcc = 0;
        alpha = alpha % 360;
        vel = new Vector(0.0,0.0,"c");
        acc = new Vector(0.0,0.0,"c");
        model = new Image("/falcon.png");
        HP = 10;
    }

    public void update(){
        System.out.println(this.HP);

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
        this.alphaVel *= 0.96;

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
        for (Explosion ex: explosions) {
            ex.show(r);
        }
    }

    public void setBoost(double boost) {
        this.acc.setP(boost, this.alpha);
    }

    public void shoot() {
        if(this.faction.equals("republic")) {
            Vehicle.republicLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
                    new Vector(this.shootForce, alpha, "p")));
        } else {
            Vehicle.empireLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, alpha, "p"), true)),
                    new Vector(this.shootForce, alpha, "p")));
        }
    }

    public boolean dead() {
        if (faction.equals("republic")) {
            for (int i = 0; i < empireLasers.size();i++) {
                Double d = this.pos.distance(empireLasers.get(i).getPos());
                if(d < 30) {
                    HP--;
                    clip.play();
                    explosions.add(new Explosion(10,5.0,pos.add(new Vector(Math.random()*10,Math.random()*360,"p"),true)));
                    empireLasers.remove(i);
                }
            }

            if(HP <= 0) {
                return true;
            } else {
                return false;
            }

        } else if (faction.equals("empire")) {
            for (int i = 0; i < republicLasers.size();i++) {
                Double d = this.pos.distance(republicLasers.get(i).getPos());
                if(d < 30) {
                    HP--;
                    clip.play();
                    explosions.add(new Explosion(10,5.0,pos.add(new Vector(Math.random()*10,Math.random()*360,"p"),true)));
                    republicLasers.remove(i);
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
}
