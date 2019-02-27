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
  private int enemyCount = 30;
  private LinkedList<HPBar> bars;
  private boolean setupAllreadyExecuted = false, executed = false, executed1 = false, executed2 = false, test = false;
  private SoundClip menuMusic, pveMusic, pvpMusic, randSound;
  private int rand1;

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

    //Player 1 is being initiated
    players.add(new Ship(new Vector(150, 150, "c"),270,"Player1", "rebel"));
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

    screen = "mainMenu";

    PvP = new Button(100, 100, " PvP");
    PvE = new Button(100, 150, " PvE");
    Coop = new Button(100, 200, " Coop");
    //settings = new Button(100, 250, "Settings");
    Exit = new Button(385,270," Exit", "/button/exitHover.png","/button/exitNoHover.png","/button/exitClicked.png");
    toMainMenu = new Button(385,270," Menu", "/button/exitHover.png", "/button/exitNoHover.png", "/button/exitClicked.png");


    sound = new Checkbox(300, 50, soundIcon);
    music = new Checkbox(430, 50, musicIcon);

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


      //Win Situations

      if(screen.equals("PvP")) {
          if(players.size() == 1 && players.get(0).getFaction().equals("rebel") && empire.size() == 0) {
              screen = "rebel";
          } else if (players.size() == 1 && players.get(0).getFaction().equals("empire") && rebel.size() == 0) {
              screen = "empire";
          }
      }

      if(screen.equals("PvE")) {
          if(players.size() == 0) {
              screen = "defeat";
          } else if(rebel.size() == 0 && empire.size() == 0) {
              screen = "victory";
          }
      }

      if(screen.equals("Coop")) {
          if(players.size() == 0) {
              screen = "defeat";
          } else if(empire.size() == 0) {
              screen = "victory";
          }
      }

      if (screen.equals("mainMenu") && players.size() > 0) {
          if(!executed1) {
              pveMusic.stop();
              pvpMusic.stop();
              if(music.testState()) menuMusic.loop();
              executed1 = true;
          }
          if (music.testAction()){
            if(music.testState()){
              menuMusic.stop();
              menuMusic.loop();
            } else {
              menuMusic.stop();
            }
          }
          setupAllreadyExecuted = false;
          executed = false;
          executed2 = false;

          PvE.update(inputPos, inputTest, sound.testState());
          PvP.update(inputPos, inputTest, sound.testState());
          Coop.update(inputPos, inputTest, sound.testState());
          //settings.update(inputPos, inputTest);
          Exit.update(inputPos, inputTest, sound.testState());

          sound.update(inputPos, inputTest, sound.testState());
          music.update(inputPos, inputTest, sound.testState());

          if(PvP.testAction()) {
              screen = "PvP";
              System.out.println("Gehe ins PvP");
              menuMusic.stop();
          }

          if(PvE.testAction()) {
              screen = "PvE";
              System.out.println("Gehe ins PvE");
              menuMusic.stop();
          }

          if(Coop.testAction()) {
              screen = "Coop";
              System.out.println("Geh in den Coop! Nicht in Migros");
              menuMusic.stop();
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

            if(music.testState()) pvpMusic.loop();

            setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("PvE")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < enemyCount; j++) {
                  //rebel.add(new Boid("rebel"));
                  empire.add(new Boid("empire"));
              }

              if(music.testState()) pveMusic.loop();

              setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("Coop")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < enemyCount; j++) {
                  //rebel.add(new Boid("rebel"));
                  empire.add(new Boid("empire"));
              }

              //Player 2 is being initiated
              players.add(new Ship(new Vector(250, 250, "c"),270,"Player2", "rebel"));
              bars.add(new HPBar(players.get(1)));

              if(music.testState()) pveMusic.loop();


              setupAllreadyExecuted = true;
          }
      }

      if(screen.equals("defeat")) {

          if(!executed) {
              players.clear();
              rebel.clear();
              empire.clear();
              bars.clear();
              players.add(new Ship(new Vector(150, 150, "c"), 270, "Player1", "rebel"));
              bars.add(new HPBar(players.get(0)));
              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              screen = "mainMenu";
              executed1 = false;
          }
      }

      if(screen.equals("victory")) {



          if(!executed) {

              rand1 = Vector.getRandomNumberInRange(0,50);

              players.clear();
              bars.clear();
              players.add(new Ship(new Vector(150, 150, "c"), 270, "Player1", "rebel"));
              bars.add(new HPBar(players.get(0)));
              executed = true;

          }

          if(rand1 == 42 && !executed2) {
              test = true;
              pveMusic.stop();
              randSound.play();
              System.out.println("hi");
              executed2 = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              screen = "mainMenu";
              executed1 = false;
          }
      }

      if(screen.equals("rebel")) {

          if(!executed) {
              players.clear();
              bars.clear();
              players.add(new Ship(new Vector(150, 150, "c"), 270, "Player1", "rebel"));
              bars.add(new HPBar(players.get(0)));
              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {

              screen = "mainMenu";
              executed1 = false;
          }
      }

      if(screen.equals("empire")) {

          if(!executed) {
              players.clear();
              bars.clear();
              players.add(new Ship(new Vector(150, 150, "c"), 270, "Player1", "rebel"));
              bars.add(new HPBar(players.get(0)));
              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              screen = "mainMenu";
              executed1 = false;
          }
      }


	  // remove Projectiles that are out of bounds
      for (Projectile empire: Vehicle.empireLasers) {
          empire.update();
      }
      for (Projectile rebel: Vehicle.rebelLasers) {
          rebel.update();
      }

      for (Boid xWing: empire) {
          xWing.peripheralVision(rebel,players);
          xWing.avoidGettingShot(rebel);
      }

      for (Boid tieFighter: rebel) {
          tieFighter.peripheralVision(empire,players);
          tieFighter.avoidGettingShot(empire);
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
          xWing.update(ge.getInput(),rebel);
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
          if(player.hit(sound.testState())) {
              player.alive = false;
          }
      }

      for (int i = 0; i < bars.size(); i++) {
          bars.get(i).update(players.get(i));
          if(!players.get(i).alive) {
            //Wenn ein Spieler tot ist wird seine HB entfernt.
              bars.remove(i);
          }
      }

      /*for (int i = 0; i < deathPos.size(); i++) {
          deathPos.add(deathVel.get(i));
      }*/



      for (int f = 0; f < rebel.size(); f++) {
          if(!rebel.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(rebel.get(f).pos);
              deathVel.add(rebel.get(f).vel);*/
            //Wenn ein Rebell tot ist wird er gelöscht.
              rebel.remove(f);
          }
      }

      for (int f = 0; f < empire.size(); f++) {
          if(!empire.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(empire.get(f).pos);
              deathVel.add(empire.get(f).vel);*/
            //Wenn ein Empiriant tot ist wird er gelöscht.
              empire.remove(f);
          }
      }

      for (int f = 0; f < players.size(); f++) {
          if(!players.get(f).alive) {
              /*deathExplosions.add(new Explosion(11,5.0));
              deathPos.add(players.get(f).pos);
              deathVel.add(players.get(f).vel);*/
            //Wenn ein Empiriant tot ist wird er gelöscht.
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
        for (Boid xWing: rebel) {
            xWing.show(r);
        }
        for(Boid tieFighter : empire) {
            tieFighter.show(r);

        }
    }

      if(screen.equals("Coop")) {
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

    if(screen.equals("rebel")) {
        r.drawImage(rebelV,240,160,0);
        toMainMenu.show(r);
    }

    if(screen.equals("empire")) {
        r.drawImage(empireV,240,160,0);
        toMainMenu.show(r);
    }




      for (Projectile projectile: Vehicle.empireLasers) {
          projectile.show(r);
      }

      for (Projectile projectile: Vehicle.rebelLasers) {
          projectile.show(r);
      }

      //explosions.add(new ImageTile("/explosion.png",16,16));


      /*for (Vector deathLine: deathVector) {
          r.drawImageTile(image, );
      }*/

      //for (ImageTile expl: explosions) {

      //}

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
