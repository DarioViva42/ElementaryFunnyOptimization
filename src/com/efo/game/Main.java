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
  private float temp = 0f;
  private LinkedList<Boid> boids = new LinkedList<>();
  private int enemyCount = 2;

  public Main() {

    image = new ImageTile("/explosion.png", 16, 16);
    mouse = new Image("/Mouse.png");
    noHover = new Image("/noHover.png");
    clicked = new Image("/clicked.png");
    hover = new Image("/hover.png");

      for (int j = 0; j < enemyCount; j++) {
          boids.add(new Boid());
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
    if(ge.getInput().isKeyDown(KeyEvent.VK_S)) {
      System.out.println("S was Pressed");
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_S)) {
      System.out.println("S was Released");
    }
    if(ge.getInput().isKeyDown(KeyEvent.VK_W)){
      ussEnterprise.setBoosting(true);
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_W)){
      ussEnterprise.setBoosting(false);
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_W)){
      ussEnterprise.setBoosting(false);
    }
    if(ge.getInput().isKeyDown(KeyEvent.VK_A)){
      ussEnterprise.setTurningL(true);
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_A)){
      ussEnterprise.setTurningL(false);
    }
    if(ge.getInput().isKeyDown(KeyEvent.VK_D)){
      ussEnterprise.setTurningR(true);
    }
    if(ge.getInput().isKeyUp(KeyEvent.VK_D)){
      ussEnterprise.setTurningR(false);
    }
    if(ge.getInput().isKeyDown(KeyEvent.VK_SPACE)){
    	ussEnterprise.shoot();
    	clip.play();
    }

	  for (int i = 0; i < Ship.projectiles.size(); i++) {
		  Ship.projectiles.get(i).update();
		  if (Ship.projectiles.get(i).getPos().getX() < -4 ||
		      Ship.projectiles.get(i).getPos().getX() > 484 ||
				  Ship.projectiles.get(i).getPos().getY() < -4 ||
				  Ship.projectiles.get(i).getPos().getY() > 324){
			  Ship.projectiles.remove(i);
		  }
	  }
	  System.out.println(Ship.projectiles.size());
    ussEnterprise.update();
    ussEnterprise.border();


    for (int j = 0; j < boids.size(); j++) {
      boids.get(j).update(ge.getInput(),boids);
      boids.get(j).border();
      System.out.println("Boid " + j + ": " + boids.get(j).peripheralVision(boids));
    }

  }



  @Override
  public void render(Engine ge, Renderer r) {

    r.drawImage(background, 240, 159, 0);

    for (int j = 0; j < starfield.length -1 ; j++) {
      starfield[j].show(r, ge.getWidth(), ge.getHeight());
      starfield[j].update();
    }


	  for (Projectile projectile:Ship.projectiles) {
		  projectile.show(r);
	  }

    /*r.drawImageTile(image,
            (ge.getInput().getMouseX())-(image.getTileW()/2),
            (ge.getInput().getMouseY())-(image.getTileH()/2),
            (int)temp, 0);*/




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

    for (int j = 0; j < boids.size(); j++) {
      boids.get(j).show(r);
    }


    ussEnterprise.show(r);
	  r.drawImage(mouse, ge.getInput().getMouseX(), ge.getInput().getMouseY(), 0);


    r.drawImage(mouse, ge.getInput().getMouseX(), ge.getInput().getMouseY(), 0);
  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here

    Vector v1 = new Vector(2, 6, "c");
    Vector v2 = new Vector(5, 1, "c");
    Vector v3 = new Vector(4, 4, "c");
    Vector v4 = new Vector(5, 5, "c");

    //double x = (this.b - line.b) / (this.m - line.m);
   // double y = this.m * x + this.b;

    ge.start();

  }

}
