package com.efo.engine;

import com.efo.engine.gfx.Image;

import java.awt.event.KeyEvent;

public class Engine implements Runnable {


  private Thread thread;


  private Window window;
  private Renderer renderer;
  private Input input;
  private AbstractGame game;

  private boolean running = false;
  private boolean antiAliasing;
  private final double UPDATE_CAP = 1.0/60.0;
  private int width = 480, height = 320;
  private float scale = 2f;
  private String title = "Star Wars Engine";
  private Image mouse;

    // Constructor -------------------------
  public Engine(AbstractGame game) {
    this.game = game;
  }

  public void start() {
    //ge == Game Engine
    window = new Window(this);
    renderer = new Renderer(this);
    input = new Input(this);
    mouse = new Image("/Mouse.png");
    antiAliasing = false;


    thread = new Thread(this);
    thread.run(); //Makes this the main thread


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
        if(input.isKeyDown(KeyEvent.VK_P) && antiAliasing){
            antiAliasing = false;
          } else {
            antiAliasing = true;
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

        if (getInput().isKey(KeyEvent.VK_DELETE)){
          renderer.drawText("FPS: " + fps, 0, 0, 0xffffffff);
        }

        renderer.drawImage(mouse, getInput().getMouseX(), getInput().getMouseY(), 0);

        if(antiAliasing) {
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

  float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  Window getWindow() {
    return window;
  }

  public Input getInput() {
    return input;
  }


}
