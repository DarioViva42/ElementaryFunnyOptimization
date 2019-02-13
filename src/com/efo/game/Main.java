package com.efo.game;

import com.efo.engine.*;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Main extends AbstractGame {

  private ImageTile image;
  private Image mouse, noHover, hover, clicked;
  private Image background;
  private SoundClip clip;
  private Star[] starfield = new Star[400];
  private Star s;
  private Ship ussEnterprise;
  private float tempX = 0f, tempY = 0f;
  private LinkedList<Boid> republic, empire;
  private int enemyCount = 10;
  private LinkedList<Vector> deathVector;
  private LinkedList<ImageTile> explosions;

  private Button settings, PvE, PvP;
  private Vector[] inputPos;
  private boolean[] inputTest;

  Vector intersection;

  public Main() {

    explosions = new LinkedList<>();
    image = new ImageTile("/explosion.png", 16, 16);
    mouse = new Image("/Mouse.png");
    noHover = new Image("/noHover.png");
    clicked = new Image("/clicked.png");
    hover = new Image("/hover.png");

    deathVector = new LinkedList<>();

    republic = new LinkedList<>();
    empire = new LinkedList<>();

      for (int j = 0; j < enemyCount; j++) {
          republic.add(new Boid("republic"));
          //empire.add(new Boid("empire"));
      }



    background = new Image("/mainMenuBackground.jpg");

    s = new Star();
    ussEnterprise = new Ship(new Vector(150, 150, "c"),270);

    for (int j = 0; j < starfield.length; j++) {
      starfield[j] = new Star();
    }

    clip = new SoundClip("/audio/explosion.wav");
    clip.setVolume(-20);

    settings = new Button(100, 100, "Settings");
    PvE = new Button(100, 150, "PvE");
    PvP = new Button(100, 200, "PvP");

    inputPos = new Vector[2];
    inputTest = new boolean[2];
  }

  @Override
  public void update(Engine ge, float dt) {
      //Player Input
    if(ge.getInput().isKeyDown(KeyEvent.VK_S)) {
      System.out.println("S was Pressed");
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_S)) {
      System.out.println("S was Released");
    }
	  if(ge.getInput().isKey(KeyEvent.VK_W)){
		  ussEnterprise.setBoost(.2);
	  } else {
		  ussEnterprise.setBoost(0);
	  }
    if(ge.getInput().isKey(KeyEvent.VK_A) && !ge.getInput().isKey(KeyEvent.VK_D)){
      ussEnterprise.turn(-.42);
    } else if(ge.getInput().isKey(KeyEvent.VK_D)){
      ussEnterprise.turn(0.42);
    } else {
      ussEnterprise.turn(0);
    }
    if(ge.getInput().isKeyDown(KeyEvent.VK_SPACE)){
    	ussEnterprise.shoot();
    	clip.play();
    }

      tempX += 1 / 5.0;
      if (tempX > 4) {
          tempX = 0;
          tempY++;
          if (tempY > 3) {
              tempY = 0;
          }
      }

      inputPos = new Vector[]{ussEnterprise.pos, new Vector(ge.getInput().getMouseX(), ge.getInput().getMouseY(), "c")};
      inputTest = new boolean[]{ge.getInput().isKey(KeyEvent.VK_ENTER), ge.getInput().isButton(1)};

      settings.update(inputPos, inputTest);
      PvE.update(inputPos, inputTest);
      PvP.update(inputPos, inputTest);

      if (PvP.testAction()){
          System.out.println("Gehe ins PvP");
      }
      if (settings.testAction()){
          System.out.println("Gehe in  Settings");
      }
      if (PvE.testAction()){
          System.out.println("Gehe ins PvE");
      }


	  // remove Projectiles that are out of bounds
      for (Projectile empire: Vehicle.empireLasers) {
          empire.update();
      }
      for (Projectile republic: Vehicle.republicLasers) {
          republic.update();
      }

      for (int i = 0; i < republic.size(); i++) {
          republic.get(i).peripheralVision(empire);
      }

      for (int i = 0; i < empire.size(); i++) {
          empire.get(i).peripheralVision(republic);
      }

      for (int j = 0; j < Vehicle.republicLasers.size(); j++) {
          if (Vehicle.republicLasers.get(j).getPos().getX() < -4 ||
                  Vehicle.republicLasers.get(j).getPos().getX() > 484 ||
                  Vehicle.republicLasers.get(j).getPos().getY() < -4 ||
                  Vehicle.republicLasers.get(j).getPos().getY() > 324) {
              Vehicle.republicLasers.remove(j);
          }
      }

      for (int i = 0; i < Vehicle.empireLasers.size(); i++) {
          if (Vehicle.empireLasers.get(i).getPos().getX() < -4 ||
                  Vehicle.empireLasers.get(i).getPos().getX() > 484 ||
                  Vehicle.empireLasers.get(i).getPos().getY() < -4 ||
                  Vehicle.empireLasers.get(i).getPos().getY() > 324) {
              Vehicle.empireLasers.remove(i);
          }
      }




    ussEnterprise.update();
    ussEnterprise.border();

      for (Boid xWing: republic) {
          xWing.update(ge.getInput(),empire);
          xWing.border();
          if(xWing.dead()) {
              xWing.alive = false;
          }
      }

      for (Boid tieFighter: empire) {
          tieFighter.update(ge.getInput(),empire);
          tieFighter.border();
          if(tieFighter.dead()) {
              tieFighter.alive = false;
          }
      }

  }



  @Override
  public void render(Engine ge, Renderer r) {

      for (int f = 0; f < republic.size(); f++) {
          if(republic.get(f).alive == false) {
              deathVector.add(new Vector(republic.get(f).pos.getX(),republic.get(f).pos.getY(),"c"));
              republic.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(empire.get(f).alive == false) {
              deathVector.add(new Vector(empire.get(f).pos.getX(),empire.get(f).pos.getY(),"c"));
              empire.remove(f);
          }
      }


    r.drawImage(background, 240, 159, 0);


    for (Star star:starfield) {
      star.show(r, ge.getWidth(), ge.getHeight());
      star.update();
    }

    for (Projectile projectile: Vehicle.empireLasers) {
      projectile.show(r);
    }

    for (Projectile projectile: Vehicle.republicLasers) {
      projectile.show(r);
    }


    settings.show(r);
    PvE.show(r);
    PvP.show(r);

    //Draw Ships
      for (Boid xWing: republic) {
          xWing.show(r);
    }
      for (Boid tieFighter: empire) {
          tieFighter.show(r);
      }

      //explosions.add(new ImageTile("/explosion.png",16,16));


      /*for (Vector deathLine: deathVector) {
          r.drawImageTile(image, );
      }*/

      //for (ImageTile expl: explosions) {

      //}

    ussEnterprise.show(r);


    r.drawImageTile(image,
      (ge.getInput().getMouseX())-(image.getTileW()/2),
      (ge.getInput().getMouseY())-(image.getTileH()/2),
      (int)tempX, (int)tempY);

    r.drawImage(mouse, ge.getInput().getMouseX(), ge.getInput().getMouseY(), 0);
  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here

    ge.start();

  }

}
