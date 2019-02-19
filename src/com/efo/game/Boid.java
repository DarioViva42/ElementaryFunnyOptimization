package com.efo.game;

import com.efo.engine.Input;
import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

public class Boid extends Vehicle {

    //Attributes
    private Vector rad, desired, steer;
    private double maxSpeed, maxForce, radiusLength, futureLocationDistance = 80, radAngle = 180;
    Double shotCap = 0.0, attackSpeed = 1.0/10.0;
    boolean alive = true;
    Vector exPos;
    LinkedList<Vehicle> currentTargets;

    int laserSound = Vector.getRandomNumberInRange(1,3);

    //Constructor
    public Boid(String Faction) {

        currentTargets = new LinkedList<>();

        pos = new Vector((int)(Math.random() * 480), (int) (Math.random() * 320), "c");
        oldPos = new Vector(0,0,"p");
        oldPos.setC(pos.getX(), pos.getY());
        vel = new Vector(0, 0, "p");
        acc = new Vector(0, 0, "p");

        rad = new Vector(0, 0, "p");

        maxSpeed = 3;
        maxForce = 0.5;
        radiusLength = 20;

        if(Faction == "rebel" || Faction == "rebel" || Faction == "republik" || Faction == "Republik") {
            model = new Image("/xWing.png");
            faction = "rebel";
        } else if(Faction == "empire" || Faction == "Empire" || Faction == "imperium" || Faction == "Imperium"){
            model = new Image("/tieFighter.png");
            faction = "empire";
        }


    }

    //Methods
    public void update(Input in, LinkedList<Boid> Faction) {
        int count = 0;

        for (Boid other: Faction) {
            Double d = this.pos.distance(other.pos);
            if((d > 0) && (d < 50)) {
                count++;
            }
        }

        applyForce(floating());
        flocking(Faction);

        vel.add(acc);
        vel.limit(maxSpeed);

        //oldPos.setC(pos.getX(), pos.getY());

        pos.add(vel);


        acc.mult(0.0);

        for (Explosion ex: explosions) {
            ex.update();
        }

        for (int i = 0; i < explosions.size(); i++) {
            if(explosions.get(i).isFinished()) {
                explosions.remove(i);
            }
        }
    }

    public void show(Renderer r) {
        r.drawImage(model, (int) this.pos.getX(), (int) this.pos.getY(), Math.toRadians(vel.getAngle()));
        for (Explosion ex: explosions) {
            ex.show(r,this.pos);
        }
    }

    public void flocking(LinkedList<Boid> boids) {
        //Vector sep = seperate(boids);
        //Vector coh = cohesion(boids);
        Vector ali = align(boids);

        //sep.mult(1.5);
        //coh.mult(1.0);
        ali.mult(1.0);

        //applyForce(sep);
        //applyForce(coh);
        applyForce(ali);


    }

    public Vector seperate(LinkedList<Boid> boids) {
        Double desiredSeperation = 25.0;
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
        float neighborDistance = 70;
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
            sum.div((count));
            sum.setLength(1);
            sum.mult(maxSpeed);
            Vector steer = sum.sub(vel,true);
            steer.limit(maxForce);
            return steer;
        } else {
            return new Vector(0,0,"p");
        }
    }

    public Vector cohesion(LinkedList<Boid> boids) {
        float neighborDistance = 50;
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

    public void peripheralVision(LinkedList<Boid> boids, LinkedList<Ship> players) {
        //Vector sum = new Vector(0,0,"p");
        Double count = 0.0;

        for (Boid other: boids) {
            Vector diff = other.pos.sub(this.pos, true);
            boolean isTargetFront = (Math.abs(diff.getAngle() - this.vel.getAngle()) + 360) % 360 < 30;
            if ((diff.getLength() > 0) && (diff.getLength() < 200) && isTargetFront) {
                //sum.add(other.vel);
                currentTargets.add(other);
                count++;
            }
        }

        for (Ship player: players) {
            if(!player.faction.equals(this.faction)) {
                Vector diff = player.pos.sub(this.pos, true);
                boolean isTargetFront = (Math.abs(diff.getAngle() - this.vel.getAngle()) + 360) % 360 < 30;
                if ((diff.getLength() > 0) && (diff.getLength() < 200) && isTargetFront) {
                    //sum.add(other.vel);
                    currentTargets.add(player);
                    count++;
                }
            }
        }

        if(count > 0) {
            shoot();
            for (Vehicle ship: currentTargets) {
                applyForce(seek(ship.getPos()));
            }
        } else if(count == 0) {
            currentTargets.clear();
        }
    }

    public boolean dead() {
        int count = 0;
        if (faction.equals("rebel")) {
            for (Projectile enemyLaser: Vehicle.empireLasers) {
                Double d = this.pos.distance(enemyLaser.getPos());
                if(d < 20) {
                    count++;
                    explosions.add(new Explosion(11,5.0));
                }
            }

            if(count > 0) {
                return true;
            } else {
                return false;
            }

        } else if (faction.equals("empire")) {
            for (Projectile enemyLaser: Vehicle.rebelLasers) {
                Double d = this.pos.distance(enemyLaser.getPos());
                if(d < 20) {
                    count++;
                    explosions.add(new Explosion(11,5.0));
                }
            }

            if(count > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public Vector floating() {
        Vector futureLocation = new Vector(0,0,"c");
        futureLocation.setP(futureLocationDistance, vel.getAngle());
        int rand = (int)(Math.random() * 40);

        if(rand < 10) {
            radAngle += 5;
        } else if(rand > 30) {
            radAngle -= 5;
        } else {

        }

        rad.setP(radiusLength, radAngle);

        desired = (futureLocation.add(rad, true));
        //desired.mult(0.5);

        steer = desired.sub(vel, true);

        steer.limit(maxForce);

        return steer;
    }



    Vector seek(Vector target) {
        Vector desired = pos.sub(target, true);

        desired.setLength(1);
        desired.mult(maxSpeed);

        Vector steer = vel.sub(desired,true);
        steer.limit(maxForce);  // Limit to maximum steering force
        return steer;
    }

    public void flee(Vector target) {
        if (this.pos.distance(target) < 80) {
            desired = (pos).sub(new Vector(target.getX(), target.getY(), "c"), true);

            desired.mult(2.0);
            desired.setLength(1);
            desired.mult(maxSpeed);

            steer = desired.sub(vel, true);

            steer.limit(maxForce);

            acc.setP(steer.getLength(), steer.getAngle());
        }
    }

    public void shoot() {
        if(faction.equals("rebel")) {
            if(shotCap >= 1) {
                rebelLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, vel.getAngle(), "p"), true)),
                        new Vector(this.shootForce, vel.getAngle(), "p")));
                sounds.get(laserSound).play();
                shotCap = 0.0;
            } else {
                shotCap += attackSpeed;
            }


        } else if(faction.equals("empire")){
            if(shotCap >= 1) {
                empireLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, vel.getAngle(), "p"), true)),
                        new Vector(this.shootForce, vel.getAngle(), "p")));
                sounds.get(laserSound).play();
                shotCap = 0.0;
            } else {
                shotCap += attackSpeed;
            }
        }
    }

    public void applyForce(Vector force) {
        acc.add(force);
    }
}
