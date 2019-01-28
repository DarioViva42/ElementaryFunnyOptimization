package com.efo.engine;
 //Selfmade
public class Vector {
  // Attributes ----------------------------------
  private double x;
  private double y;
  private double vLength;
  private double alpha;


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
        this.alpha = ya;
        System.out.println(alpha);

        calcCoord(xl, ya);
        break;
      default:
        System.out.println("Da lief etwas falsch in Vector");
    }

  }


  // Methods -------------------------------------
  private void calcPol(double x, double y){
    if (x > 0.0) {
      this.alpha = (Math.toDegrees(Math.atan(y / x))+360)%360;
    } else if (x < 0.0) {
      this.alpha = 180 + Math.toDegrees(Math.atan(y/x));
    } else if(x == 0 && y > 0) {
      this.alpha = 90;
    } else if(x == 0 && y < 0) {
      this.alpha = 270;
    } else if(x == 0 && y == 0) {
      this.alpha = 0;
    } else {
      System.out.println("Da lief etwas sehr falsch in calcPol");
    }
    this.vLength = (double)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
  }

  private void calcCoord(double l, double a){
    this.x = l*Math.cos(Math.toRadians(a));
    this.y = l*Math.sin(Math.toRadians(a));
  }

  public void add(Vector b) {
    this.x += b.getX();
    this.y += b.getY();
    calcPol(this.x, this.y);
  }

  public Vector add(Vector b, boolean test) {
    Vector giveVec;
    double x = this.getX() + b.getX();
    double y = this.getY() + b.getY();
    giveVec = new Vector(x, y, "C");
    return giveVec;
  }

  double distance(Vector b) {
    double distance = Math.sqrt(Math.pow(this.x-b.getX(),2)+Math.pow(this.y-b.getY(),2));
    return distance;

  }

  public double getLength() {
    return this.vLength;
  }

  public double getAngle() {
    System.out.println(this.alpha);
    return this.alpha;

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

  public void setP(double vLength, double angle) {
    this.vLength = vLength;
    this.alpha = angle;
    calcCoord(vLength, angle);
  }
}
