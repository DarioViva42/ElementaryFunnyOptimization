package com.efo.game;

import com.efo.engine.AbstractGame;
import com.efo.engine.Engine;
import com.efo.engine.Renderer;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import java.awt.event.KeyEvent;

public class Main extends AbstractGame {

  private Image image;
  private SoundClip clip;


  public Main() {
    image = new Image("/test.png");
    //clip = new SoundClip("/audo/test.wav")
    //clip.setVolume(-20);
  }

  @Override
  public void update(Engine ge, float dt) {
    if(ge.getInput().isKeyDown(KeyEvent.VK_A)) {
      System.out.println("A was Pressed");
    }
  }

  @Override
  public void render(Engine ge, Renderer r) {
    r.drawImage(image,(ge.getInput().getMouseX())-(image.getW()/2),(ge.getInput().getMouseY())-(image.getH()/2));
  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here
    ge.start();

  }

}
