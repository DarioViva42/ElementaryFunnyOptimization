package com.efo.engine;

public class Engine implements Runnable {


  private Thread thread;
  private Window window;
  private Renderer renderer;

  private boolean running = false;
  private final double UPDATE_CAP = 1.0/60.0;
  private int width = 320, height = 240;
  private float scale = 4f;
  private String title = "Star Wars Engine";

  public Engine() {

  }

  public void start() {
    //ge == Game Engine
    window = new Window(this);
    renderer = new Renderer(this);

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


        //TODO: Update game
        if(frameTime >= 1.0){
          frameTime = 0;
          fps = frames;
          frames = 0;
          System.out.println("FPS: " + fps);
        }
      }

      if (render) {

        renderer.clear();
        //TODO: Render Game

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

}
