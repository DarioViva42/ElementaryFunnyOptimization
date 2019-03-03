//Our intellectual property

package com.efo.engine;

import java.util.Random;

public class Vector {
  // Attributes ----------------------------------
  private double x;
  private double y;
  private double vLength;
  private double angle;


  // Constructor ---------------------------------
  public Vector(double xl, double ya, String test) {
    switch(test){
      case "c": case "C": case "coord": case "Coord": case "coordinate": case "Coordinate":
        this.x = xl;
        this.y = ya;

        calcPol(xl, ya);
        break;
      case "p": case "P": case "Polar": case "polar":
        this.vLength = xl;
        this.angle = ya;

        calcCoord(xl, ya);
        break;
      default:
        System.out.println("Da lief etwas falsch in Vector");
    }
  }


  // Methods -------------------------------------
  private void calcPol(double x, double y){
    if (x > 0.0) {
      angle = (Math.toDegrees(Math.atan(y / x))+360)%360;
    } else if (x < 0.0) {
      angle = 180 + Math.toDegrees(Math.atan(y/x));
    } else if(x == 0 && y > 0.0) {
      angle = 90;
    } else if(x == 0 && y < 0.0) {
      angle = 270;
    } else if(x == 0 && y == 0.0) {
      angle = 0.0;
    } else {
      System.out.println("Da lief etwas sehr falsch in calcPol.");
    }
    vLength = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
  }

  private void calcCoord(double l, double a){
    x = l * Math.cos(Math.toRadians(a));
    y = l * Math.sin(Math.toRadians(a));
  }

  public void add(Vector other) {
    x += other.getX();
    y += other.getY();
    calcPol(x, y);
  }

  public void sub(Vector other) {
    x -= other.getX();
    y -= other.getY();
    calcPol(x, y);
  }

  public void mult(Double Factor) {
    x *= Factor;
    y *= Factor;
    calcPol(this.x, this.y);
  }

  public void div(Double Nenner) {
    if(Nenner != 0) {
      double f = this.x / Nenner;
      double g = this.y / Nenner;
      calcPol(f, g);
    }
  }

  public Vector add(Vector other, boolean test) {
    Vector giveVec;
    double x = this.getX() + other.getX();
    double y = this.getY() + other.getY();
    giveVec = new Vector(x, y, "c");
    return giveVec;
  }

  public Vector sub(Vector other, boolean test) {
    Vector giveVec;
    double x = this.getX() - other.getX();
    double y = this.getY() - other.getY();
    giveVec = new Vector(x, y, "c");
    return giveVec;
  }

  public double distance(Vector b) {
    return Math.sqrt(Math.pow(this.x-b.getX(),2)+Math.pow(this.y-b.getY(),2));
  }

  public double getLength() {
    return this.vLength;
  }
  public double getAngle() {
    return angle;

  }
  public double getX() {
    return this.x;
  }
  public double getY() {
      return this.y;
  }

  public void setC(double x, double y) {
    this.x = x;
    this.y = y;
    calcPol(x, y);
  }
  public void setP(double length, double angle) {
    vLength = length;
    this.angle = angle;
    calcCoord(vLength, angle);
  }
  public void setLength(int length) {
    vLength = length;
    calcCoord(length,angle);
  }

  public void limit(Double limit) {
    if(this.vLength > limit) {
      this.vLength = limit;
      calcCoord(vLength,angle);
    }
  }

  public static int getRandomNumberInRange(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }
}
