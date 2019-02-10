package com.efo.engine;

//Selfmade

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
    vLength = (double)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
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
      x /= Nenner;
      y /= Nenner;
      calcPol(x, y);
    } else {

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

  public Vector mult(Vector other, boolean test) {
    Vector giveVec;
    double x = this.getX() * other.getX();
    double y = this.getY() * other.getY();
    giveVec = new Vector(x, y, "c");
    return giveVec;
  }
  
  public static boolean collisionTest(Vector newV, Vector oldV, Vector newW, Vector oldW){
    double deltaVy = oldV.getY()-newV.getY();
    double deltaVx = oldV.getX()-newV.getX();
    double deltaWy = oldW.getY()-newW.getY();
    double deltaWx = oldW.getX()-newW.getX();

    if(deltaVx==0 && deltaVy==0 && deltaWx==0 && deltaWy==0){
      return false;
    }

    // berechne Steigungen der Geradengleichungen
    double vm = deltaVy/deltaVx;
    double wm = deltaWy/deltaWx;
    if (vm == wm){
      return false;
    }

    // berechne Y-Achsenabschnitte
    double vb = oldV.getY() - vm * oldV.getX();
    double wb = oldW.getY() - wm * oldW.getX();

    // berechene Schnittpunkt
    double x = Math.abs((vb - wb) / (vm - wm));
    double y = vm * x + vb;

    Vector intersection = new Vector(x, y, "c");

    double distance = 12.0;

    // finde heraus ob Schnittpunkt höchstens <distance> von den zurückgelegten Strecken entfernt ist.

    System.out.println((int)intersection.distance(oldV) +" "+(int)intersection.distance(oldW)+" "+
        (int)intersection.distance(newV)+" "+ (int)intersection.distance(newW));
    return (
      x < Math.max(newV.getX(), oldV.getX()) &&
      x > Math.min(newV.getX(), oldV.getX()) &&
      x < Math.max(newW.getX(), oldW.getX()) &&
      x > Math.min(newW.getX(), oldW.getX()) &&
      y < Math.max(newV.getY(), oldV.getY()) &&
      y > Math.min(newV.getY(), oldV.getY()) &&
      y < Math.max(newW.getY(), oldW.getY()) &&
      y > Math.min(newW.getY(), oldW.getY()) ||
          Math.min(Math.min(intersection.distance(oldV), intersection.distance(oldW)),
                   Math.min(intersection.distance(newV), intersection.distance(newW))) < distance
    );

  }

  public double distance(Vector b) {
    double distance = Math.sqrt(Math.pow(this.x-b.getX(),2)+Math.pow(this.y-b.getY(),2));
    return distance;
  }

  public double getLength() {
    return this.vLength;
  }
  public double getAngle() {
    return angle;

  }
  public double getX() {
    double giveX = this.x;
    return giveX;
  }
  public double getY() {
      double giveY = this.y;
      return giveY;
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
    if(vLength > limit) {
      vLength = limit;
      calcCoord(vLength,angle);
    }
  }

}
