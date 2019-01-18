class Star {

  // Attributes ---------------------------------
  Vector pos;
  Vector vel;
  float starSize;

  //random speed
  float sVel;
  //static angle for the Stars
  float sAngle = 140;

  //Star size
  float stSiLower = 1.5;
  float stSiUpper = 1.8;
  float stSiPow = 10;
  float stSiModeration = 120;

  //starSize to velocity Mapping
  float lowerStarSize = 0.5;
  float upperStarSize = 3;
  float lowerVel = 0;
  float upperVel = 0.5;

  // Constructor --------------------------------
  Star() {
    float sX = random(0,width);
    float sY = random(0,height);
    this.pos = new Vector(sX,sY,"c");
    
    newSizeVel();
  }

  Star(int direction) {
    float sX;
    float sY;
    switch(direction) {
      case 1:

         sX = 0;
         sY = random(0,height);

        this.pos = new Vector(sX,sY,"c");
        newSizeVel();
        break;

      case 2:

         sX = random(0,width);
         sY = 0;

        this.pos = new Vector(sX,sY,"c");
        newSizeVel();
        break;

      case 3:

         sX = width;
         sY = random(0,height);

        this.pos = new Vector(sX,sY,"c");
        newSizeVel();
        break;

      case 4:

         sX = random(0,width);
         sY = height;

        this.pos = new Vector(sX,sY,"c");
        newSizeVel();
        break;

      default:
        System.out.println("Problem with the new Stars");
    }
  }


  // Methods ------------------------------------
  void show() {
    fill(#fffeed);
    noStroke();
    ellipse(this.pos.getX(),this.pos.getY(),starSize,starSize);

    //wenn rechts raus
    if(this.pos.getX() > width) {
      this.pos.setC(0,random(0,height));
      newSizeVel();

    //wenn links raus
    } else if(this.pos.getX() < 0) {
      this.pos.setC(width,random(0,height));
      newSizeVel();

    //wenn unten raus
    } else if(this.pos.getY() > height) {
      this.pos.setC(random(0,width),0);
      newSizeVel();

    //wenn oben raus
    } else if(this.pos.getY() < 0){
      this.pos.setC(random(0,width),height);
      newSizeVel();
    }
  }

  void update() {
    this.pos.add(this.vel);
  }

  void newSizeVel() {
    // starSizeModeration für Darios bildschirm halbieren!(60), für Luis(120)
    this.starSize = (pow(random(stSiLower,stSiUpper),stSiPow))/stSiModeration;

    //speed mapped to size of Star
    this.sVel = map(starSize,lowerStarSize,upperStarSize,lowerVel,upperVel);
    this.vel = new Vector(sVel,sAngle,"p");
  }

}
