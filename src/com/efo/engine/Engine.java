package com.efo.engine;

public class Engine implements Runnable {

  private Thread thread;

  private boolean running = false;
  private final double UPDATE_CAP = 1.0/60.0;

  public Engine() {

  }

  public void start() {
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

        //TODO: Render Game
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



}
