package com.efo.game;

import com.efo.engine.AbstractGame;
import com.efo.engine.Engine;
import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
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
  private int enemyCount = 2;
  private LinkedList<Vector> deathVector;

  Vector intersection;

  public Main() {

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
          empire.add(new Boid("empire"));
      }



    background = new Image("/mainMenuBackground.jpg");

    s = new Star();
    ussEnterprise = new Ship(new Vector(150, 150, "c"),270);

    for (int j = 0; j < starfield.length; j++) {
      starfield[j] = new Star();
    }

    clip = new SoundClip("/audio/explosion.wav");
    clip.setVolume(-20);
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

     //Framework for Image Tile
    tempX += 1/5.0;
    if(tempX > 4) {
        tempX = 0;
        tempY ++;
        if(tempY > 3) {
            tempY = 0;
        }
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
              deathVector.add(new Vector(republic.get(f).vel.getLength(),republic.get(f).vel.getAngle(),"p"));
              republic.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(empire.get(f).alive == false) {
              deathVector.add(new Vector(empire.get(f).vel.getLength(),empire.get(f).vel.getAngle(),"p"));
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





    //Test Button abfrage
    if(ge.getInput().getMouseX() > 180 && ge.getInput().getMouseX() < 305 && ge.getInput().getMouseY() > 30 && ge.getInput().getMouseY() < 70 || ussEnterprise.getPos().getX() > 180 && ussEnterprise.getPos().getX() < 315 && ussEnterprise.getPos().getY() > 30 && ussEnterprise.getPos().getY() < 70) {
      if(!ge.getInput().isButton(1) && !ge.getInput().isKey(KeyEvent.VK_ENTER)) {
        r.drawImage(hover, 250, 50, 0);
        r.drawText("Settings", 195, 38, 0x8f858af2);
      }
      if(ge.getInput().isButton(1) || ge.getInput().isKey(KeyEvent.VK_ENTER)) {
        r.drawImage(clicked, 250, 50, 0);
        r.drawText("Settings", 195, 38, 0x6F858af2);
      }
    } else {
        r.drawImage(noHover, 250, 50, 0);
        r.drawText("Settings", 195, 38, 0xFF858af2);
    }

    //Draw Ships
      for (Boid xWing: republic) {
          xWing.show(r);
    }
      for (Boid tieFighter: empire) {
          tieFighter.show(r);
      }



      for (Vector deathLine: deathVector) {
          r.drawImageTile(image, 50,50,(int)tempX,(int)tempY);
      }

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
