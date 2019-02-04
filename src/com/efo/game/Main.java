package com.efo.game;

import com.efo.engine.AbstractGame;
import com.efo.engine.Engine;
import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Main extends AbstractGame {

  private ImageTile image;
  private Image image2, noHover, hover, clicked;
  private Image background;
  private SoundClip clip;
  private double i;
  private Star[] starfield = new Star[400];
  private Star s;
  private Ship ussEnterprise;
  private float temp = 0f;

  public Main() {

    image = new ImageTile("/explosion.png", 16, 16);
    image2 = new Image("/test2.png");
    noHover = new Image("/noHover.png");
    clicked = new Image("/clicked.png");
    hover = new Image("/hover.png");



    background = new Image("/mainMenuBackground.jpg");
    i = 0;

    s = new Star();
    ussEnterprise = new Ship(new Vector(150, 150, "c"),0.1);
    for (int j = 0; j < starfield.length; j++) {
      starfield[j] = new Star();
    }

    //clip = new SoundClip("/audo/test.wav")
    //clip.setVolume(-20);
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
    temp += dt * 20;

    if(temp >= 4){
      temp = 0;
    }
    ussEnterprise.update();

  }



  @Override
  public void render(Engine ge, Renderer r) {

    r.drawImage(background, 240, 160, 0);

    /*for (int j = 0; j < starfield.length -1 ; j++) {
      starfield[j].show(r, ge.getWidth(), ge.getHeight());
      starfield[j].update();
    }*/




    /*r.drawImageTile(image,
            (ge.getInput().getMouseX())-(image.getTileW()/2),
            (ge.getInput().getMouseY())-(image.getTileH()/2),
            (int)temp, 0);*/


    r.drawImage(image2, ge.getInput().getMouseX(), ge.getInput().getMouseY(), i);


    //i += .01;

    if(ge.getInput().getMouseX() > 180 && ge.getInput().getMouseX() < 305 && ge.getInput().getMouseY() > 30 && ge.getInput().getMouseY() < 70 || ussEnterprise.getPos().getX() > 180 && ussEnterprise.getPos().getX() < 315 && ussEnterprise.getPos().getY() > 30 && ussEnterprise.getPos().getY() < 70) {
      if(!ge.getInput().isButton(1) && !ge.getInput().isKey(KeyEvent.VK_ENTER)) {
        r.drawImage(hover, 250, 50, 0);
        r.drawText("Hover, ob du's glaubst oder", 195, 38, 0xffff00ff);
      }
      if(ge.getInput().isButton(1) || ge.getInput().isKey(KeyEvent.VK_ENTER)) {
        r.drawImage(clicked, 250, 50, 0);
        r.drawText("pressed", 195, 38, 0xffff00ff);
      }
    } else {
        r.drawImage(noHover, 250, 50, 0);
        r.drawText("nicht auf Knopf", 195, 38, 0xffff00ff);
    }


    ussEnterprise.show(r);




  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here


    ge.start();

  }

}
