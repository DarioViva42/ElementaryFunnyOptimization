package com.efo.engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Engine implements Runnable {


  private Thread thread;
  private Window window;
  private Renderer renderer;
  private Input input;
  private AbstractGame game;

  private boolean running = false;
  private boolean aa; // Anti-Aliasing
  private final double UPDATE_CAP = 1.0/120.0;
  private int width = 320, height = 280;
  private float scale = 2f;
  private String title = "Star Wars Engine";

    // Constructor -------------------------
  public Engine(AbstractGame game) {
    this.game = game;
  }

  public void start() {
    //ge bedeutet Game Engine
    window = new Window(this);
    renderer = new Renderer(this);
    input = new Input(this);

    aa = false;
    thread = new Thread(this);
    thread.run(); //Makes this main thread
  }

  public void stop() {

  }

  public void run() {
    running = true;

    boolean render = false;
    double firstTime = 0;
    double lastTime = System.nanoTime()/1000000000.0; //makes this miliseconds
    double passedTime = 0;
    double unprocessedTime = 0;

    double frameTime = 0;
    int frames = 0;
    int fps = 0;


    while(running) {

      render = false;
      firstTime = System.nanoTime() / 1000000000.0;
      passedTime = firstTime - lastTime;
      lastTime = firstTime;

      unprocessedTime += passedTime;
      frameTime+= passedTime;

      while (unprocessedTime >= UPDATE_CAP) {
        unprocessedTime -= UPDATE_CAP; //Makes sure missed updates are caught
        render = true;

        game.update(this,(float)UPDATE_CAP);

        // Schalte zwischen Aliasing und anti-Aliasing
        if (input.isKeyDown(KeyEvent.VK_P)){
          if(aa){
            aa = false;
          } else{
            aa = true;
          }
        }

        input.update();


        input.update();

        if(frameTime >= 1.0){
          frameTime = 0;
          fps = frames;
          frames = 0;
          // This happens every 1 second. Maybe useful for console or log entries.
        }
      }

      if (render) {





        renderer.clear();

        game.render(this,renderer);

        renderer.drawText("FPS: " + fps, 0, 0, 0xffffffff);

        if(aa) {
          renderer.antiAliasing();
        }

        window.update();
        frames++;







      } else {
        try {
          Thread.sleep(1); //Sleep when not rendering. Saves CPU
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    dispose();
  }

  private void dispose() {

  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Window getWindow() {
    return window;
  }

  public Input getInput() {
    return input;
  }


}
