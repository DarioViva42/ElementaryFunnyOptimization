package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

import java.util.LinkedList;

public class Doll {
    Vector vel;
    Vector pos;
    int part;
    LinkedList<Image> dolls;


    Doll(Vector pos, Vector vel, String model) {
        this.pos = pos;
        this.vel = vel;

        if (model.equals("falcon")) {
            dolls.add(new Image("/dollModel/falcon"));
        } else if (model.equals("interceptor")) {
            dolls.add(new Image("/dollModel/interceptor"));
        } else if (model.equals("xWing")) {
            dolls.add(new Image("/dollModel/xWing1"));
            dolls.add(new Image("/dollModel/xWing2"));
        } else if (model.equals("tieFighter")) {
            dolls.add(new Image("/dollModel/tieFighter1"));
            dolls.add(new Image("/dollModel/tieFighter2"));
        }
    }

    void update() {

    }

    void show(Renderer r) {

        r.drawImage(dolls.get(1),(int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.vel.getAngle()));
    }
}
