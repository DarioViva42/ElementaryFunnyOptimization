package com.efo.game;

import com.efo.engine.*;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Main extends AbstractGame {

  private ImageTile image;
  private Image background;
  private Star[] starfield = new Star[400];
  private Star s;
  private float tempX = 0f, tempY = 0f;
  private LinkedList<Boid> republic, empire;
  private LinkedList<Ship> players;
  private int enemyCount = 5;
  private LinkedList<Vector> deathVector;
  private String screen;

  private Button settings, PvE, PvP, Exit;
  private Vector[] inputPos;
  private boolean[] inputTest;

  public Main() {


    image = new ImageTile("/explosion.png", 16, 16);

    deathVector = new LinkedList<>();

    republic = new LinkedList<>();
    empire = new LinkedList<>();

      for (int j = 0; j < enemyCount; j++) {
          republic.add(new Boid("republic"));
          //empire.add(new Boid("empire"));
      }

    players = new LinkedList<>();

    background = new Image("/mainMenuBackground.jpg");

    s = new Star();

    players.add(new Ship(new Vector(150, 150, "c"),270,"Player1"));
    players.add(new Ship(new Vector(250, 250, "c"),270,"Player2"));

    for (int j = 0; j < starfield.length; j++) {
      starfield[j] = new Star();
    }

    screen = "mainMenu";

    PvP = new Button(100, 100, " PvP");
    PvE = new Button(100, 150, " PvE");
    settings = new Button(100, 200, "Settings");
    Exit = new Button(385,270," Exit", "/exitHover.png","/exitNoHover.png","/exitClicked.png");

    inputPos = new Vector[2];
    inputTest = new boolean[2];
  }

  @Override
  public void update(Engine ge, float dt) {






      for(Ship player: players) {
          //Player 1 Input
          if(player.playerName.equals("Player1")) {
              if (ge.getInput().isKeyDown(KeyEvent.VK_S)) {
                  System.out.println("S was Pressed");
              }
              if (ge.getInput().isKeyUp(KeyEvent.VK_S)) {
                  System.out.println("S was Released");
              }
              if (ge.getInput().isKey(KeyEvent.VK_W)) {
                  player.setBoost(.2);
              } else {
                  player.setBoost(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_A) && !ge.getInput().isKey(KeyEvent.VK_D)) {
                  player.turn(-.42);
              } else if (ge.getInput().isKey(KeyEvent.VK_D)) {
                  player.turn(0.42);
              } else {
                  player.turn(0);
              }
              if (ge.getInput().isKeyDown(KeyEvent.VK_C)) {
                  player.shoot();

              }
          }

          if(player.playerName.equals("Player2")) {
              //Player 2 Input
              if (ge.getInput().isKeyDown(KeyEvent.VK_J)) {
                  System.out.println("S was Pressed");
              }
              if (ge.getInput().isKeyUp(KeyEvent.VK_J)) {
                  System.out.println("S was Released");
              }
              if (ge.getInput().isKey(KeyEvent.VK_U)) {
                  player.setBoost(.2);
              } else {
                  player.setBoost(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_H) && !ge.getInput().isKey(KeyEvent.VK_RIGHT)) {
                  player.turn(-.42);
              } else if (ge.getInput().isKey(KeyEvent.VK_K)) {
                  player.turn(0.42);
              } else {
                  player.turn(0);
              }
              if (ge.getInput().isKeyDown(KeyEvent.VK_B)) {
                  player.shoot();
              }
          }
      }



      if(players.size() > 0) {
          inputPos = new Vector[]{players.get(0).pos, new Vector(ge.getInput().getMouseX(), ge.getInput().getMouseY(), "c")};
          inputTest = new boolean[]{ge.getInput().isKey(KeyEvent.VK_ENTER), ge.getInput().isButton(1)};
      }

      if (screen.equals("mainMenu")) {
          PvE.update(inputPos, inputTest);
          PvP.update(inputPos, inputTest);
          settings.update(inputPos, inputTest);
          Exit.update(inputPos, inputTest);

          if (PvP.testAction()) {
              screen = "PvP";
              System.out.println("Gehe ins PvP");
          }
          if (settings.testAction()) {
              screen = "Settings";
              System.out.println("Gehe in  Settings");
          }
          if (PvE.testAction()) {
              screen = "PvE";
              System.out.println("Gehe ins PvE");
          }
          if (Exit.testAction()) {
              System.exit(0);
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
          republic.get(i).peripheralVision(empire,players);
      }

      for (int i = 0; i < empire.size(); i++) {
          empire.get(i).peripheralVision(republic,players);
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




      for (Boid xWing: republic) {
          xWing.update(ge.getInput(),republic);
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

      for (Ship player: players) {
          player.update();
          player.border();
          if(player.dead()) {
              player.alive = false;
          }
      }

  }



  @Override
  public void render(Engine ge, Renderer r) {

      for (int f = 0; f < republic.size(); f++) {
          if(!republic.get(f).alive) {
              //deathVector.add(new Vector(republic.get(f).pos.getX(),republic.get(f).pos.getY(),"c"));
              republic.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(!empire.get(f).alive) {
              //deathVector.add(new Vector(empire.get(f).pos.getX(),empire.get(f).pos.getY(),"c"));
              empire.remove(f);
          }
      }

      for (int f = 0; f < players.size(); f++) {
          if(!players.get(f).alive) {
              //deathVector.add(new Vector(players.get(f).pos.getX(),players.get(f).pos.getY(),"c"));
              players.remove(f);
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



    if (screen.equals("mainMenu")) {
        PvE.show(r);
        PvP.show(r);
        settings.show(r);
        Exit.show(r);
    }

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
      for (Ship player: players) {
          player.show(r);
      }


  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here

    ge.start();

  }

}
