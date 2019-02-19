package com.efo.game;

import com.efo.engine.*;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;

import javax.sound.sampled.Line;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Main extends AbstractGame {

  private Image background, victory, soundIcon, musicIcon;
  private Star[] starField = new Star[400];
  private Star s;
  private LinkedList<Boid> republic, empire;
  private LinkedList<Ship> players;
  private int enemyCount = 3;
  private LinkedList<HPBar> bars;
  private boolean setupAllreadyExecuted = false, victoryAllreadyExecuted = false;

  /*private LinkedList<Vector> deathPos;
  private LinkedList<Vector> deathVel;
  private LinkedList<Explosion> deathExplosions;*/

  private String screen;

  private Checkbox sound, music;
  private Button PvE, PvP, Exit, Coop , toMainMenu;
  //private Button settings;
  private Vector[] inputPos;
  private boolean[] inputTest;

  public Main() {

      Vehicle.sounds.add(new SoundClip("/audio/explosion.wav"));
      Vehicle.sounds.add(new SoundClip("/audio/laser1.wav"));
      Vehicle.sounds.add(new SoundClip("/audio/laser2.wav"));
      Vehicle.sounds.add(new SoundClip("/audio/laser3.wav"));
      Vehicle.sounds.add(new SoundClip("/audio/laser4.wav"));
      Vehicle.sounds.add(new SoundClip("/audio/laser5.wav"));

      Vehicle.sounds.get(0).setVolume(-20);
      Vehicle.sounds.get(1).setVolume(-20);
      Vehicle.sounds.get(2).setVolume(-20);
      Vehicle.sounds.get(3).setVolume(-20);
      Vehicle.sounds.get(4).setVolume(-20);
      Vehicle.sounds.get(5).setVolume(-20);



    /*deathPos = new LinkedList<>();
    deathVel = new LinkedList<>();
    deathExplosions = new LinkedList<>();*/

    republic = new LinkedList<>();
    empire = new LinkedList<>();

    players = new LinkedList<>();
    bars = new LinkedList<>();

    //Player 1 is being initiated
    players.add(new Ship(new Vector(150, 150, "c"),270,"Player1", "republic"));
    bars.add(new HPBar(players.get(0)));



    background = new Image("/mainMenuBackground.jpg");
    victory = new Image("/victory.png");
	  soundIcon = new Image("/soundIcon.png");
	  musicIcon = new Image("/musicIcon.png");

    s = new Star();






    for (int j = 0; j < starField.length; j++) {
      starField[j] = new Star();
    }

    screen = "mainMenu";

    PvP = new Button(100, 100, " PvP");
    PvE = new Button(100, 150, " PvE");
    Coop = new Button(100, 200, " Coop");
    //settings = new Button(100, 200, "Settings");
    Exit = new Button(385,270," Exit", "/exitHover.png","/exitNoHover.png","/exitClicked.png");
    toMainMenu = new Button(385,270," Menu", "/exitHover.png", "/exitNoHover.png", "/exitClicked.png");


		sound = new Checkbox(300, 50, soundIcon);
	  music = new Checkbox(430, 50, musicIcon);

    inputPos = new Vector[2];
    inputTest = new boolean[2];
  }

  // ------------------------------------UPDATE---------------------------------------------

  @Override
  public void update(Engine ge, float dt) {

      //Win Situations

      if(screen.equals("PvP")) {
          if(players.size() == 1 && players.get(0).getFaction().equals("republic") && empire.size() == 0) {
              System.out.println("The Republic has Won!");
          } else if (players.size() == 1 && players.get(0).getFaction().equals("empire") && republic.size() == 0) {
              System.out.println("The Empire has Won!");
          }
      }

      if(screen.equals("PvE")) {
          if(players.size() == 0) {
              System.out.println("You Lost!");
          } else if(republic.size() == 0 && empire.size() == 0) {
              screen = "victory";
          }
      }


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
              if (ge.getInput().isKey(KeyEvent.VK_C)) {
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
              if (ge.getInput().isKey(KeyEvent.VK_B)) {
                  player.shoot();
              }
          }
      }



      if(players.size() > 0) {
          inputPos = new Vector[]{players.get(0).pos, new Vector(ge.getInput().getMouseX(), ge.getInput().getMouseY(), "c")};
          inputTest = new boolean[]{ge.getInput().isKey(KeyEvent.VK_ENTER), ge.getInput().isButton(1)};
      }

      if (screen.equals("mainMenu") && players.size() > 0) {
          setupAllreadyExecuted = false;

          PvE.update(inputPos, inputTest);
          PvP.update(inputPos, inputTest);
          Coop.update(inputPos, inputTest);
          //settings.update(inputPos, inputTest);
          Exit.update(inputPos, inputTest);

          sound.update(inputPos, inputTest);
          music.update(inputPos, inputTest);

          if(PvP.testAction()) {
              screen = "PvP";
              System.out.println("Gehe ins PvP");
          }

          if(PvE.testAction()) {
              screen = "PvE";
              System.out.println("Gehe ins PvE");
          }

          if(Coop.testAction()) {
              screen = "Coop";
              System.out.println("Geh in den Coop! Nicht in die Migros");
          }

          /*if(settings.testAction()) {
              screen = "Settings";
              System.out.println("Gehe in  Settings");
          }*/

          if(Exit.testAction()) {
              System.exit(0);
          }
      }

      if(screen.equals("PvP")) {
          if(!setupAllreadyExecuted) {

              //Player 2 is being initiated
              players.add(new Ship(new Vector(250, 250, "c"),270,"Player2", "empire"));
              bars.add(new HPBar(players.get(1)));


            setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("PvE")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < enemyCount; j++) {
                  //republic.add(new Boid("republic"));
                  empire.add(new Boid("empire"));
              }



              setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("Coop")) {
          if(!setupAllreadyExecuted) {


          }
      }

      if(screen.equals("victory")) {
          toMainMenu.update(inputPos,inputTest);

          if(toMainMenu.testAction()) {
              screen = "mainMenu";
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



      //Deathcheck, update xWings pos and check if out of border
      for (Boid xWing: republic) {
          xWing.update(ge.getInput(),republic);
          xWing.border();
          if(xWing.dead()) {
              xWing.alive = false;
          }
      }

      //Deathcheck, update TieFighter pos and check if out of border
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
          if(player.hit()) {
              player.alive = false;
          }
      }

      for (int i = 0; i < bars.size(); i++) {
          bars.get(i).update(players.get(i));
          if(!players.get(i).alive) {
              bars.remove(i);
          }
      }

      /*for (int i = 0; i < deathPos.size(); i++) {
          deathPos.add(deathVel.get(i));
      }*/



      for (int f = 0; f < republic.size(); f++) {
          if(!republic.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(republic.get(f).pos);
              deathVel.add(republic.get(f).vel);*/
              republic.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(!empire.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(empire.get(f).pos);
              deathVel.add(empire.get(f).vel);*/
              empire.remove(f);
          }
      }

      for (int f = 0; f < players.size(); f++) {
          if(!players.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(players.get(f).pos);
              deathVel.add(players.get(f).vel);*/
              players.remove(f);
          }
      }
  }



  @Override
  public void render(Engine ge, Renderer r) {

      /*for (int i = 0; i < deathPos.size(); i++) {
          deathExplosions.get(i).show(r,deathPos.get(i).add(deathVel.get(i),true));
      }*/






    r.drawImage(background, 240, 159, 0);


    for (Star star:starField) {
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
        Coop.show(r);
        //settings.show(r);
        Exit.show(r);

        sound.show(r);
        music.show(r);
    }

    //Draw Ships
    if(screen.equals("PvE")) {
        for (Boid xWing : republic) {
            xWing.show(r);
        }
        for (Boid tieFighter : empire) {
            tieFighter.show(r);
        }
    }

    if(screen.equals("victory")) {
        r.drawImage(victory,240,160,0);
        toMainMenu.show(r);
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

      for (HPBar bar : bars) {
          bar.show(r);
      }



  }

  public static void main(String[] args) {

    Engine ge = new Engine(new Main());
    //Initiate Game Settings here

    ge.start();

  }

}
