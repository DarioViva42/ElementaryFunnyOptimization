class Console {


  // Attributes ---------------------------------
  int xVal;
  int yVal;
  float speedVal;
  
  int textSize = 0;
  
  //Restriction Down
  int rest1 = 49;
  int rest2 = 50;
  
  // Constructor --------------------------------



  // Methods ------------------------------------

  void showConsole(Ship enterprise) {
    
    this.xVal = (int)enterprise.pos.getX();
    this.yVal = (int)enterprise.pos.getY();
    this.speedVal = enterprise.vel.getLength();
    
    xVal = (int)round(xVal);
    yVal = (int)round(yVal);
    speedVal = ((float)round(speedVal*10))/10;
    
    String firstLine = Integer.toString(xVal);
    String secondLine = Integer.toString(yVal);
    String thirdLine = Float.toString(speedVal);
    
    String line = firstLine + "\n" + secondLine + "\n" + thirdLine;
    
    float x1 = enterprise.pos.getX()+22;
    float y1 = enterprise.pos.getY();
    
    //left
    if(x1 <= 24) {
      x1 = 25;
    }
    
    //right
    if(x1 >= width-59) {
      x1 = width-60;
    }
    
    //down
    if(y1 >= height-rest1) {
      y1 = height-rest2;
    }
    
    //up
    if(y1 <= 34) {
      y1 = 35;
    }
    
    fill(255);
    
    if(textSize == 0) {
      textSize(12);
      text(line, x1, y1);
    } else if(textSize == 1) {
      textSize(20);
      text(line, x1, y1);
    } else {
      
    }
    
  }

  void changeSize() {
    
    switch(textSize) {
      case 0: textSize++;
      break;
      
      case 1: textSize++;
      break;
      
      case 2: textSize = 0;
      break;
      
    }
  }
}
