
/*
Project still heavily in developement
*/

float turnRate = 8;
Ship ussEnterprise;
Map space;
Console ourConsole;

void setup() {

  size(1280,720);
  
  ussEnterprise = new Ship(new Vector(width/2, height/2, "c"),270);
  
  ourConsole = new Console();
  space = new Map(600,10);
  
  
}




void draw() {

  background(0);

  space.bgGraphic();
  
  

  ussEnterprise.show();
  ussEnterprise.update();

  ourConsole.showConsole(ussEnterprise);
}

// Controll of Player ------------------------
void keyPressed() {
  switch(key){
    case ' ': 
      //ussEnterprise.shoot();
      break;
    case 'w':
      ussEnterprise.isBoosting = true;
      break;
    case 'a':
      ussEnterprise.isTurningL = true;
      break;
    case 'd':
      ussEnterprise.isTurningR = true;
      break;
      
    case 'c':
      this.ourConsole.changeSize();
      break;
  }
}

void keyReleased() {
  switch(key){
    case 'w':
      ussEnterprise.isBoosting = false;
      break;
    case 'a': 
      ussEnterprise.isTurningL = false;
      break;
    case 'd':
      ussEnterprise.isTurningR = false;
      break;
  }
}




  // Attributes ---------------------------------



  // Constructor --------------------------------



  // Methods ------------------------------------



  
