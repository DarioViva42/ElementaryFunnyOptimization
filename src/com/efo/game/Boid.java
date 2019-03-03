//Our intellectual interpretation of Rian Reynolds Algorithm for flocking Simulations to imitate Star Wars Space Ships

package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;
import java.util.LinkedList;

public class Boid extends Vehicle {

    //Attributes
    private Vector rad, steer;
    private double maxSpeed, maxForce, radiusLength, radAngle = 180;
    private Double shotCap = 0.0;
    boolean alive = true;
    private LinkedList<Vehicle> currentTargets;
    private int HP = 1;
    private boolean test = true;

    private int laserSound = Vector.getRandomNumberInRange(1,3);

    //Constructor -----------------------------
    public Boid(String Faction) {

        currentTargets = new LinkedList<>();

        if(Faction.equals("rebel")) {
            pos = new Vector(Vector.getRandomNumberInRange(0, 20), Vector.getRandomNumberInRange(10, 310), "c");
            vel = new Vector(0, 0, "p");
        } else if(Faction.equals("empire")) {
            pos = new Vector(Vector.getRandomNumberInRange(460, 480), Vector.getRandomNumberInRange(10, 310), "c");
            vel = new Vector(0, 180, "p");
        }

        acc = new Vector(0, 0, "p");

        rad = new Vector(0, 0, "p");

        maxSpeed = 4;
        maxForce = 0.5;
        radiusLength = 20;

        if(Faction.equals("rebel") || Faction.equals("Rebel") || Faction.equals("republik") || Faction.equals("Republik")) {
            model = new Image("/ships/xWing.png");
            faction = "rebel";
        } else if(Faction.equals("empire") || Faction.equals("Empire") || Faction.equals("imperium") || Faction.equals("Imperium")){
            model = new Image("/ships/tieFighter.png");
            faction = "empire";
        }
    }

    //Methods ------------------------------

    //update Boid according to the behaviour and by some random movement
    public void update(LinkedList<Boid> Faction) {

        applyForce(floating());
        starWarsShipBehaviour(Faction);

        vel.add(acc);
        vel.limit(maxSpeed);

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

    //render the Boid and explosions (explosions only matter if boid has more than 1 HP)
    public void show(Renderer r) {
        r.drawImage(model, (int) this.pos.getX(), (int) this.pos.getY(), Math.toRadians(vel.getAngle()));
        for (Explosion ex: explosions) {
            ex.show(r,this.pos);
        }
    }

    //all methods influencing behaviour are collected here
    private void starWarsShipBehaviour(LinkedList<Boid> boids) {
        Vector ali = align(boids);

        ali.mult(1.0);

        applyForce(ali);
    }

    //Boids fly towards the average direction of local flockmates
    private Vector align(LinkedList<Boid> boids) {
        float neighborDistance = 70;
        Vector sum = new Vector(0,0, "p");
        double count = 0.0;
        for (Boid other: boids) {
            double d = this.pos.distance(other.pos);
            if((d > 0) && (d < neighborDistance)) {
                sum.add(other.vel);
                count++;
            }
        }
        if(count > 0) {
            sum.div((count));
            sum.setLength(1);
            sum.mult(maxSpeed / 1.8);
            Vector steer = sum.sub(vel,true);
            steer.limit(maxForce);
            return steer;
        } else {
            return new Vector(0,0,"p");
        }
    }

    //if there is an enemy in boids peripheral vision then shoot
    void peripheralVision(LinkedList<Boid> boids, LinkedList<Ship> players, boolean sound) {
        double count = 0.0;

        for (Boid other: boids) {
            Vector diff = other.pos.sub(this.pos, true);
            boolean isTargetFront = (Math.abs(diff.getAngle() - this.vel.getAngle()) + 360) % 360 < 50;
            if ((diff.getLength() > 0) && (diff.getLength() < 160) && isTargetFront) {
                currentTargets.add(other);
                count++;
            }
        }

        for (Ship player: players) {
            if (!player.faction.equals(this.faction)) {
                Vector diff = player.pos.sub(this.pos, true);
                boolean isTargetFront = (Math.abs(diff.getAngle() - this.vel.getAngle()) + 360) % 360 < 50;
                if ((diff.getLength() > 0) && (diff.getLength() < 180) && isTargetFront) {
                    currentTargets.add(player);
                    count++;
                }
            }
        }

        if(count > 0) {
            shoot(sound);
            for (Vehicle ship: currentTargets) {
                applyForce(seek(ship.getPos()));
            }
        } else if(count == 0) {
            currentTargets.clear();
        }
    }

    //if there is a boid in inverse peripheral vision then flee
    void avoidGettingShot(LinkedList<Boid> boids, LinkedList<Ship> players) {
        Vector flee;
        double count = 0.0;

        for (Boid other: boids) {
            Vector diff = other.pos.sub(this.pos, true);
            boolean isTargetBack = (Math.abs(diff.getAngle() - (this.vel.getAngle() + 180)) + 360) % 360 < 30;
            if ((diff.getLength() > 0) && (diff.getLength() < 120) && isTargetBack) {
                currentTargets.add(other);
                count++;
            }
        }

        for (Ship player: players) {
            if(!player.getFaction().equals(this.faction)) {
                Vector diff = player.pos.sub(this.pos, true);
                boolean isTargetBack = (Math.abs(diff.getAngle() - (this.vel.getAngle() + 180)) + 360) % 360 < 30;
                if ((diff.getLength() > 0) && (diff.getLength() < 140) && isTargetBack) {
                    currentTargets.add(player);
                    count++;
                }
            }
        }

        if(count > 0) {

                if(test) {
                    int random = (int) (Math.random() * 2);
                    if (random == 0) {
                        flee = new Vector(vel.getAngle() + 90, 1, "p");
                    } else {
                        flee = new Vector(vel.getAngle() - 90, 1, "p");
                    }
                    test = false;

                    flee.mult(2.0);
                    flee.setLength(1);
                    flee.mult(maxSpeed);
                    steer = flee.sub(vel, true);
                    steer.limit(maxForce);
                }
                applyForce(steer);

        } else if(count == 0) {
            test = true;
        }
    }

    //if boid is hit by a laser decrease health / if boid has no health left return true
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
                    rebelLasers.remove(i);
                }
            }

            return (HP <= 0);
        } else {
            return false;
        }
    }

    //some random movement
    private Vector floating() {
        double futureLocationDistance = 80;

        Vector desired;
        Vector futureLocation = new Vector(0,0,"c");
        futureLocation.setP(futureLocationDistance, vel.getAngle());
        int rand = (int)(Math.random() * 40);

        if(rand < 10) {
            radAngle += 5;
        } else if(rand > 30) {
            radAngle -= 5;
        }

        rad.setP(radiusLength, radAngle);

        desired = (futureLocation.add(rad, true));

        steer = desired.sub(vel, true);

        steer.limit(maxForce);

        return steer;
    }

    //hunt target (fly after)
    private Vector seek(Vector target) {
        Vector desired = pos.sub(target, true);

        desired.setLength(1);
        desired.mult(2 * maxSpeed);

        Vector steer = vel.sub(desired,true);
        steer.limit(maxForce);
        return steer;
    }

    //shoot (add projectile to LinkedList)
    private void shoot(boolean sound) {
        double attackSpeed = 1.0/20.0;

        if(faction.equals("rebel")) {
            if(shotCap >= 1) {
                rebelLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, vel.getAngle(), "p"), true)),
                        new Vector(this.shootForce, vel.getAngle(), "p"),"rebel"));
                if(sound)
                    sounds.get(laserSound).play();
                shotCap = 0.0;
            } else {
                shotCap += attackSpeed;
            }


        } else if(faction.equals("empire")){
            if(shotCap >= 1) {
                empireLasers.add(new Projectile((new Vector(this.pos.getX(), this.pos.getY(), "c").add(new Vector(10, vel.getAngle(), "p"), true)),
                        new Vector(this.shootForce, vel.getAngle(), "p"),"empire"));
                if(sound)
                    sounds.get(laserSound).play();
                shotCap = 0.0;
            } else {
                shotCap += attackSpeed;
            }
        }
    }

    //Method to apply force to accaleration
    private void applyForce(Vector force) {
        acc.add(force);
    }

    //Craig reynolds flocking Algorithm (not used in our project except for one rule. Alignment)
    private void flocking(LinkedList<Boid> boids) {
        Vector sep = seperate(boids);
        Vector coh = cohesion(boids);
        Vector ali = align(boids);

        sep.mult(1.5);
        coh.mult(1.0);
        ali.mult(1.0);

        applyForce(sep);
        applyForce(coh);
        applyForce(ali);


    }

    //first rule of Craig Reynolds
    private Vector cohesion(LinkedList<Boid> boids) {
        float neighborDistance = 50;
        Vector sum = new Vector(0,0,"p");
        Double count = 0.0;

        for (Boid other: boids) {
            double d = this.pos.distance(other.pos);
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

    //second rule of Craig Reynolds
    private Vector seperate(LinkedList<Boid> boids) {
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
}
