package com.efo.game;

import com.efo.engine.AbstractGame;
import com.efo.engine.Engine;
import com.efo.engine.Renderer;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.event.KeyEvent;

public class Main extends AbstractGame {

  private ImageTile image;
  private SoundClip clip;


  public Main() {
    image = new ImageTile("/Explosion.png", 16, 16);
    //clip = new SoundClip("/audo/test.wav")
    //clip.setVolume(-20);
  }

  @Override
  public void update(Engine ge, float dt) {
    if(ge.getInput().isKeyDown(KeyEvent.VK_A)) {
      System.out.println("A was Pressed");
    }
    temp += dt;

    if(temp >= 4){
      temp = 0;
    }
  }

  float temp = 0f;

  @Override
  public void render(Engine ge, Renderer r) {
    r.drawImageTile(image,
            (ge.getInput().getMouseX())-(image.getTileW()/2),
            (ge.getInput().getMouseY())-(image.getTileH()/2),
            (int)temp, 0);
    System.out.println((int)temp + " " + temp);
  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here
    ge.start();

  }

}
