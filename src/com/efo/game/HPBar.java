package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

public class HPBar {

    Vector pos;
    boolean test = false;
    int maxHP, x, length = 25, angle = 90;
    Double tenPercentHP;
    String status = "";
    Image bar = new Image("/fullBar.png");

    public HPBar(Ship ship) {
        x = 1;
        tenPercentHP = ship.getHP()/10.0;
        maxHP = ship.getHP();
        pos = ship.pos.add(new Vector(length,angle,"p"),true);
    }

    public void update(Ship ship) {
        this.pos = ship.pos.add(new Vector(length,angle,"p"),true);

        if(ship.getHP() <= (maxHP - (x * tenPercentHP))) {
            test = true;
            x++;
        }

        if(x == 1) {
            if(test) test = false; bar = new Image("/fullBar.png");
        } else if(x == 2) {
            if(test) test = false; bar = new Image("/90Bar.png");
        } else if(x == 3) {
            if(test) test = false; bar = new Image("/80Bar.png");
        } else if(x == 4) {
            if(test) test = false; bar = new Image("/70Bar.png");
        } else if(x == 5) {
            if(test) test = false; bar = new Image("/60Bar.png");
        } else if(x == 6) {
            if(test) test = false; bar = new Image("/50Bar.png");
        } else if(x == 7) {
            if(test) test = false; bar = new Image("/40Bar.png");
        } else if(x == 8) {
            if(test) test = false; bar = new Image("/30Bar.png");
        } else if(x == 9) {
            if(test) test = false; bar = new Image("/20Bar.png");
        } else if(x == 10) {
            if(test) test = false; bar = new Image("/10Bar.png");
        }
    }

    public void show(Renderer r) {
        r.drawImage(bar,(int)pos.getX(),(int)pos.getY(),0.0);
    }
}
