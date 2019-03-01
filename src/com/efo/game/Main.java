package com.efo.game;

import com.efo.engine.*;
import com.efo.engine.audio.SoundClip;
import com.efo.engine.gfx.Image;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Main extends AbstractGame {

  private Image background, victory, defeat, royal, rebelV, empireV;
  private Star[] starField = new Star[400];
  private LinkedList<Boid> rebel, empire;
  private LinkedList<Ship> players;
  private LinkedList<HPBar> bars;
  private boolean setupAllreadyExecuted = false, executed = false, executed1 = false, executed2 = false, test = false, firstTimeInMainMenu = true;
  private SoundClip menuMusic, pveMusic, pvpMusic, randSound;
  private int rand1;
  private LinkedList<Doll> dolls;

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

      menuMusic = new SoundClip("/audio/mainMenuMusic.wav");
      pveMusic = new SoundClip("/audio/pveMusic.wav");
      pvpMusic= new SoundClip("/audio/pvpMusic.wav");
      randSound = new SoundClip("/audio/randSound.wav");

      menuMusic.setVolume(-20);
      pveMusic.setVolume(-20);
      pvpMusic.setVolume(-20);

    /*deathPos = new LinkedList<>();
    deathVel = new LinkedList<>();
    deathExplosions = new LinkedList<>();*/

    rebel = new LinkedList<>();
    empire = new LinkedList<>();

    players = new LinkedList<>();
    bars = new LinkedList<>();
    dolls = new LinkedList<>();

    //Player 1 is being initiated
    players.add(new Ship(new Vector(240, 160, "c"),90,"Player1", "rebel"));
    bars.add(new HPBar(players.get(0)));



    background = new Image("/screens/mainMenuBackground.jpg");
    victory = new Image("/screens/victory.png");
    royal = new Image("/screens/royal.png");
    defeat = new Image("/screens/defeat.png");
    rebelV = new Image("/screens/rebelWin.png");
    empireV = new Image("/screens/empireWin.png");

    Image soundIcon = new Image("/etc/soundIcon.png");
    Image musicIcon = new Image("/etc/musicIcon.png");


    for (int j = 0; j < starField.length; j++) {
      starField[j] = new Star();
    }

    screen = "PvE";

    inputPos = new Vector[2];
    inputTest = new boolean[2];
  }

  // ------------------------------------UPDATE---------------------------------------------

  @Override
  public void update(Engine ge, float dt) {



      for(Ship player: players) {
          //Player 1 Input
          if(player.playerName.equals("Player1")) {
              if (ge.getInput().isKey(KeyEvent.VK_W)) {
                  player.setBoost(.2);
              } else {
                  player.setBoost(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_A) && !ge.getInput().isKey(KeyEvent.VK_D)) {
                  player.turn(-.42);
              } else if (ge.getInput().isKey(KeyEvent.VK_D) && !ge.getInput().isKey(KeyEvent.VK_A)) {
                  player.turn(0.42);
              } else {
                  player.turn(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_C)) {
                  player.shoot(sound.testState());

              }
          }

          if(player.playerName.equals("Player2")) {
              if (ge.getInput().isKey(KeyEvent.VK_U)) {
                  player.setBoost(.2);
              } else {
                  player.setBoost(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_H) && !ge.getInput().isKey(KeyEvent.VK_K)) {
                  player.turn(-.42);
              } else if (ge.getInput().isKey(KeyEvent.VK_K) && !ge.getInput().isKey(KeyEvent.VK_H)) {
                  player.turn(0.42);
              } else {
                  player.turn(0);
              }
              if (ge.getInput().isKey(KeyEvent.VK_B)) {
                  player.shoot(sound.testState());
              }
          }
      }



      if(players.size() > 0) {
          inputPos = new Vector[]{players.get(0).pos, new Vector(ge.getInput().getMouseX(), ge.getInput().getMouseY(), "c")};
          inputTest = new boolean[]{ge.getInput().isKey(KeyEvent.VK_ENTER), ge.getInput().isButton(1)};
      }


      // --------------------------------------SCREENS----------------------------------------


      //Win Situation

      if(screen.equals("PvE")) {
          if(players.size() == 0) {
              screen = "defeat";
          } else if(empire.size() == 0) {
              screen = "victory";
          }
      }


      if(screen.equals("PvE")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < 20; j++) {
                  rebel.add(new Boid("rebel"));
              }
              for(int j = 0; j < 20; j++) {
                  empire.add(new Boid("empire"));
              }

              if(music.testState()) pveMusic.loop();

              setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("defeat")) {

          if(!executed) {
              players.add(new Ship(new Vector(240, 160, "c"),90,"Player1", "rebel"));
              bars.add(new HPBar(players.get(0)));
              rebel.clear();
              empire.clear();

              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              //return = false!!! ---------------------------------------
          }
      }

      if(screen.equals("victory")) {

          if(!executed) {

              rand1 = Vector.getRandomNumberInRange(0,50);
              executed = true;

          }

          if(rand1 == 42 && !executed2) {
              test = true;
              pveMusic.stop();
              randSound.play();
              executed2 = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              //return = true!!! ---------------------------------------
          }
      }


	  // remove Projectiles that are out of bounds
      for (Projectile empire: Vehicle.empireLasers) {
          empire.update();
      }
      for (Projectile rebel: Vehicle.rebelLasers) {
          rebel.update();
      }


      for (int i = 0; i < dolls.size(); i++) {
          if (dolls.get(i).border()) dolls.remove(i);
      }

      for (Doll wrack : dolls) {
          wrack.updateMult();
      }


      for (Boid xWing: rebel) {
          xWing.peripheralVision(empire,players, sound.testState());
      }

      for (Boid tieFighter: empire) {
          tieFighter.peripheralVision(rebel,players, sound.testState());
      }

      for (int j = 0; j < Vehicle.rebelLasers.size(); j++) {
          if (Vehicle.rebelLasers.get(j).getPos().getX() < -4 ||
                  Vehicle.rebelLasers.get(j).getPos().getX() > 484 ||
                  Vehicle.rebelLasers.get(j).getPos().getY() < -4 ||
                  Vehicle.rebelLasers.get(j).getPos().getY() > 324) {
            //Wenn ein Laser den Bildschirm verlässt wird er gelöscht.
              Vehicle.rebelLasers.remove(j);
          }
      }

      for (int i = 0; i < Vehicle.empireLasers.size(); i++) {
          if (Vehicle.empireLasers.get(i).getPos().getX() < -4 ||
                  Vehicle.empireLasers.get(i).getPos().getX() > 484 ||
                  Vehicle.empireLasers.get(i).getPos().getY() < -4 ||
                  Vehicle.empireLasers.get(i).getPos().getY() > 324) {
            //Wenn ein Laser den Bildschirm verlässt wird er gelöscht.
              Vehicle.empireLasers.remove(i);
          }
      }



      //Deathcheck, update xWings pos and check if out of border
      for (Boid xWing: rebel) {
          xWing.update(rebel);
          xWing.border();
          if(xWing.hit(sound.testState())) {
              xWing.alive = false;
          }
      }

      //Deathcheck, update TieFighter pos and check if out of border
      for (Boid tieFighter: empire) {
          tieFighter.update(empire);
          tieFighter.border();
          if(tieFighter.hit(sound.testState())) {
              tieFighter.alive = false;
          }
      }

      for (Ship player: players) {
          player.update();
          player.border();
          if(player.hit(sound.testState())) {
              player.alive = false;
          }
      }

      for (int i = 0; i < bars.size(); i++) {
          bars.get(i).update(players.get(i));
          if(!players.get(i).alive) {
              bars.remove(i);
          }
      }


      for (int f = 0; f < rebel.size(); f++) {
          if(!rebel.get(f).alive) {
              dolls.add(new Doll(rebel.get(f),"xWing"));
              rebel.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(!empire.get(f).alive) {
              dolls.add(new Doll(empire.get(f),"tieFighter"));
              empire.remove(f);
          }
      }


      for (Ship player: players) {
          if(!player.alive && player.getFaction() == "rebel") {
              dolls.add(new Doll(player,"falcon"));
          } else if(!player.alive && player.getFaction() == "empire") {
              dolls.add(new Doll(player,"interceptor"));
          }
      }


      for (int f = 0; f < players.size(); f++) {
          if(!players.get(f).alive) {
              players.remove(f);
          }
      }
  }



  @Override
  public void render(Engine ge, Renderer r) {


    r.drawImage(background, 240, 159, 0);

    for (Star star:starField) {
      star.show(r, ge.getWidth(), ge.getHeight());
      star.update();
    }


    //Draw Boids
    if(screen.equals("PvE")) {
        for (Boid xWing: rebel) {
            xWing.show(r);
        }
        for(Boid tieFighter : empire) {
            tieFighter.show(r);
        }
    }


    if(screen.equals("defeat")) {
       r.drawImage(defeat,240,160,0);
       toMainMenu.show(r);
    }

    if(screen.equals("victory")) {
        if(!test) {
            r.drawImage(victory, 240, 160, 0);
        } else {
            r.drawImage(royal,240,160,0);
        }
        toMainMenu.show(r);
    }





      for (Projectile projectile: Vehicle.empireLasers) {
          projectile.show(r);
      }

      for (Projectile projectile: Vehicle.rebelLasers) {
          projectile.show(r);
      }

      for (Doll wrack: dolls) {
          wrack.showMult(r);
      }

      for (HPBar bar : bars) {
          bar.show(r);
      }

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
