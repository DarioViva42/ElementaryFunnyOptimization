package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

public class HPBar {

    Vector pos;
    boolean test = false;
    int maxHP, x, length = 25;
    Double tenPercentHP;
    String status = "";
    Image bar = new Image("/fullBar.png");

    public HPBar(Ship ship) {
        x = 1;
        tenPercentHP = ship.getHP()/10.0;
        maxHP = ship.getHP();
        pos = ship.pos.add(new Vector(length,270,"p"),true);
    }

    public void update(Ship ship) {
        this.pos = ship.pos.add(new Vector(length,270,"p"),true);

        if(ship.getHP() <= (maxHP - (x * tenPercentHP))) {
            test = true;
            x++;
        }

        if(x == 1) {
            if(test) test = false; bar = new Image("/fullBar.png");
            //full
        } else if(x == 2) {
            if(test) test = false; bar = new Image("/90Bar.png");
            //90
        } else if(x == 3) {
            if(test) test = false; bar = new Image("/80Bar.png");
            //80
        } else if(x == 4) {
            if(test) test = false; bar = new Image("/70Bar.png");
            //70
        } else if(x == 5) {
            if(test) test = false; bar = new Image("/60Bar.png");
            //60
        } else if(x == 6) {
            if(test) test = false; bar = new Image("/50Bar.png");
            //50
        } else if(x == 7) {
            if(test) test = false; bar = new Image("/40Bar.png");
            //40
        } else if(x == 8) {
            if(test) test = false; bar = new Image("/30Bar.png");
            //30
        } else if(x == 9) {
            if(test) test = false; bar = new Image("/20Bar.png");
            //20
        } else if(x == 10) {
            if(test) test = false; bar = new Image("/10Bar.png");
            //10
        }
    }

    public void show(Renderer r) {
        r.drawImage(bar,(int)pos.getX(),(int)pos.getY(),0.0);
    }
}
