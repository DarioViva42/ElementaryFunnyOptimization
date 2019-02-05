package com.efo.engine;

//Selfmade

public class Tupel {

    int x;
    int y;

    public Tupel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Tupel other) {
        return (this.x == other.x) && (this.y == other.y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
