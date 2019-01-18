class Ship {

  // Attributes ----------------------------------
  Vector pos;   // Position
  Vector gunPos;// Position der Waffe (Vorne)
  Vector vel;   // Geschwidigkeit
  Vector acc;   // Beschleunigung
  
  boolean isTurningL = false;
  boolean isTurningR = false;
  boolean isBoosting = false;
  float alphaVel;  // Die Winkelgeschwindigkeit des Schiffes
  float alphaAcc;  // Die Winkelbeschleunigung
  float alpha;     // Der Winkel
  float maxTurnAcc = 0.2;

  
  //Ship build up
  int head = 20;
  int wings = 10;

  // Constructor ---------------------------------
  Ship(Vector pos, float alpha) {
    this.pos = pos;
    this.alphaVel = 0;
    this.alphaAcc = 0;
    this.alpha = alpha % 360;
    vel = new Vector(0.0,0.0,"c");
    acc = new Vector(0.0,0.0,"c");
  }

  // Methods -------------------------------------
  void show(){
    fill(#FF0000);
    noStroke();
    Vector front = pos.add(new Vector(head, alpha, "p"), false);
    float xf = front.getX();
    float yf = front.getY();

    Vector left = pos.add(new Vector(wings, alpha + 120, "p"),true);
    float xl = left.getX();
    float yl = left.getY();
    
    Vector right = pos.add(new Vector(wings, alpha + 240, "p"),true);
    float xr = right.getX();
    float yr = right.getY();

    triangle(xf, yf, xl, yl, xr, yr);
  }
  
  void turn() {
    if (isTurningL && isTurningR){
      this.alphaAcc = 0;
    } else if (isTurningL){
      this.alphaAcc = -maxTurnAcc;
    } else if (isTurningR){
      this.alphaAcc = maxTurnAcc;
    } else {
      this.alphaAcc = 0;
    }
  }

  /*Projectile shoot() {
    Projectile geschoss = new Projectile(this.gunPos, this.alpha);
    return geschoss;
  }*/

  Vector getPos() {
    Vector position = this.pos;
    return position;
  }

  void boost(){
    if(isBoosting){
      this.acc.setP(0.1, this.alpha);
    } else {
      this.acc.setP(0, this.alpha);
    }
  }

  float getAbsVel() {
    float absoluteVelocity = this.vel.getLength();
    return absoluteVelocity;
  }

  void update(){
    boost();
    turn();
    this.vel.add(this.acc);
    this.pos.add(this.vel);
    this.alphaVel += alphaAcc;
    this.alpha = (this.alpha + alphaVel) % 360;

    //drag
    float l = this.vel.getLength();
    float a = this.vel.getAngle();
    System.out.println(a);
    //Velocity Drag
    this.vel.setP(0.992 * l, a);
    //Turning Drag
    this.alphaVel *= 0.9855; 
  }

}
