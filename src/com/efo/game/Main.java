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
  private boolean setupAllreadyExecuted = false, executed = false, executed1 = false, executed2 = false, executed3 = false, test = false, firstTimeInMainMenu = true;
  private SoundClip menuMusic, pveMusic, pvpMusic, randSound;
  private int rand1;
  private LinkedList<Doll> dolls;
  LinkedList<Image> backgrounds;

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

    rebel = new LinkedList<>();
    empire = new LinkedList<>();

    players = new LinkedList<>();
    bars = new LinkedList<>();
    dolls = new LinkedList<>();

    backgrounds = new LinkedList<>();

    //Player 1 is being initiated
    players.add(new Ship(new Vector(240, 160, "c"),90,"Player1", "rebel"));
    bars.add(new HPBar(players.get(0)));



    victory = new Image("/screens/victory.png");
    royal = new Image("/screens/royal.png");
    defeat = new Image("/screens/defeat.png");
    rebelV = new Image("/screens/rebelWin.png");
    empireV = new Image("/screens/empireWin.png");

    backgrounds.add(new Image("/screens/spaceBG1.png"));
    backgrounds.add(new Image("/screens/spaceBG2.png"));
    backgrounds.add(new Image("/screens/spaceBG3.png"));
    backgrounds.add(new Image("/screens/spaceBG4.png"));
    backgrounds.add(new Image("/screens/spaceBG5.png"));
    backgrounds.add(new Image("/screens/mainMenuBackground.jpg"));

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

      //Input tests
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


      //Input enhancements
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
          } else if(empire.size() == 0) {
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


      //Screen handling

      //Main Menu Setup
      if (screen.equals("mainMenu") && players.size() > 0) {

          if(!executed1) {
              if(!firstTimeInMainMenu) {
                  players.clear();
                  bars.clear();
                  players.add(new Ship(new Vector(240, 160, "c"), 90, "Player1", "rebel"));
                  bars.add(new HPBar(players.get(0)));
              }
              rebel.clear();
              empire.clear();

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
          executed3 = false;

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
              firstTimeInMainMenu = false;
          }

          if(PvE.testAction()) {
              screen = "PvE";
              System.out.println("Gehe ins PvE");
              menuMusic.stop();
              firstTimeInMainMenu = false;
          }

          if(Coop.testAction()) {
              screen = "Coop";
              System.out.println("Geh in den Coop! Nicht in Migros");
              menuMusic.stop();
              firstTimeInMainMenu = false;
          }

          if(Exit.testAction()) {
              System.exit(0);
          }
      }

      //PvP Setup
      if(screen.equals("PvP")) {
          if(!setupAllreadyExecuted) {

            //Player 2 is being initiated
            players.add(new Ship(new Vector(420, 160, "c"),180,"Player2", "empire"));
            bars.add(new HPBar(players.get(1)));

            if(music.testState()) pvpMusic.loop();

            setupAllreadyExecuted = true;
          }
          if(ge.getInput().isKey(KeyEvent.VK_ESCAPE)){
              Vehicle.rebelLasers.clear();
              Vehicle.empireLasers.clear();
              pvpMusic.stop();

              executed1 = false;
              screen = "mainMenu";
          }
      }

      //PvE Setup
      if(screen.equals("PvE")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < 2; j++) {
                  rebel.add(new Boid("rebel"));
              }
              for(int j = 0; j < 5; j++) {
                  empire.add(new Boid("empire"));
              }


              if(music.testState()) pveMusic.loop();

              setupAllreadyExecuted = true;
          }
          if(ge.getInput().isKey(KeyEvent.VK_ESCAPE)){
              rebel.clear();
              Vehicle.rebelLasers.clear();
              empire.clear();
              Vehicle.empireLasers.clear();
              pveMusic.stop();

              executed1 = false;
              screen = "mainMenu";
          }
      }

      //Coop Setup
      if(screen.equals("Coop")) {
          if(!setupAllreadyExecuted) {
              for(int j = 0; j < 8; j++) {
                  //rebel.add(new Boid("rebel"));
                  empire.add(new Boid("empire"));
              }

              //Player 2 is being initiated
              players.add(new Ship(new Vector(250, 250, "c"),270,"Player2", "rebel"));
              bars.add(new HPBar(players.get(1)));

              if(music.testState()) pveMusic.loop();


              setupAllreadyExecuted = true;
          }

          if(ge.getInput().isKey(KeyEvent.VK_ESCAPE)){
              Vehicle.rebelLasers.clear();
              empire.clear();
              Vehicle.empireLasers.clear();
              pveMusic.stop();

              executed1 = false;
              screen = "mainMenu";
          }
      }

      //Defeat Screen / PvE and Coop
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
              screen = "mainMenu";
              executed1 = false;
          }
      }

      //Victory Screen / PvE and Coop
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
              screen = "mainMenu";
              executed1 = false;
          }
      }

      //rebel Victory Screen / PvP
      if(screen.equals("rebel")) {

          if(!executed) {

              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {

              screen = "mainMenu";
              executed1 = false;
          }
      }

      //empire Victory Screen / PvP
      if(screen.equals("empire")) {

          if(!executed) {
              executed = true;
          }

          toMainMenu.update(inputPos,inputTest, sound.testState());

          if(toMainMenu.testAction()) {
              screen = "mainMenu";
              executed1 = false;
          }
      }


	  //remove Projectiles that are out of bounds
      for (Projectile empire: Vehicle.empireLasers) {
          empire.update();
      }
      for (Projectile rebel: Vehicle.rebelLasers) {
          rebel.update();
      }

      //remove Dolls that are out of bounds
      for (int i = 0; i < dolls.size(); i++) {
          if (dolls.get(i).border()) dolls.remove(i);
      }

      //Update dolls according to their velocity
      for (Doll wrack : dolls) {
          wrack.update();
      }

      //Hunt enemies / rebel
      for (Boid xWing: rebel) {
          xWing.peripheralVision(empire,players, sound.testState());
          //xWing.avoidGettingShot(empire, players);
      }

      //Hunt enemies / empire
      for (Boid tieFighter: empire) {
          tieFighter.peripheralVision(rebel,players, sound.testState());
          //tieFighter.avoidGettingShot(rebel,players);
      }

      //Delete laser if out of bounds / rebel
      for (int j = 0; j < Vehicle.rebelLasers.size(); j++) {
          if (Vehicle.rebelLasers.get(j).getPos().getX() < -4 ||
              Vehicle.rebelLasers.get(j).getPos().getX() > 484 ||
              Vehicle.rebelLasers.get(j).getPos().getY() < -4 ||
              Vehicle.rebelLasers.get(j).getPos().getY() > 324) {
              Vehicle.rebelLasers.remove(j);
          }
      }

      //Delete laser if out of bounds / empire
      for (int i = 0; i < Vehicle.empireLasers.size(); i++) {
          if (Vehicle.empireLasers.get(i).getPos().getX() < -4 ||
              Vehicle.empireLasers.get(i).getPos().getX() > 484 ||
              Vehicle.empireLasers.get(i).getPos().getY() < -4 ||
              Vehicle.empireLasers.get(i).getPos().getY() > 324) {
              Vehicle.empireLasers.remove(i);
          }
      }

      //Deathcheck, update xWings according to velocity and check if out of border
      for (Boid xWing: rebel) {
          xWing.update(rebel);
          xWing.border();
          if(xWing.hit(sound.testState())) {
              xWing.alive = false;
          }
      }

      //Deathcheck, update tieFighters according to velocity and check if out of border
      for (Boid tieFighter: empire) {
          tieFighter.update(empire);
          tieFighter.border();
          if(tieFighter.hit(sound.testState())) {
              tieFighter.alive = false;
          }
      }

      //Deathcheck players, Check if players are out of bounds and update according to velocity
      for (Ship player: players) {
          player.update();
          player.border();
          if(player.hit(sound.testState())) {
              player.alive = false;
          }
      }

      //Update bars according to ship
      for (int i = 0; i < bars.size(); i++) {
          bars.get(i).update(players.get(i));
          if(!players.get(i).alive) {
              bars.remove(i);
          }
      }

      //If boid died make a Doll for it and remove it / rebel
      for (int f = 0; f < rebel.size(); f++) {
          if(!rebel.get(f).alive) {
              dolls.add(new Doll(rebel.get(f),"xWing"));
              rebel.remove(f);
          }
      }

      //If boid died make a Doll for it and remove it / empire
      for (int f = 0; f < empire.size(); f++) {
          if(!empire.get(f).alive) {
              dolls.add(new Doll(empire.get(f),"tieFighter"));
              empire.remove(f);
          }
      }

      //If Ship died make a Doll for it
      for (Ship player: players) {
          if(!player.alive && player.getFaction() == "rebel") {
              dolls.add(new Doll(player,"falcon"));
          } else if(!player.alive && player.getFaction() == "empire") {
              dolls.add(new Doll(player,"interceptor"));
          }
      }

      //Remove player if he dies
      for (int f = 0; f < players.size(); f++) {
          if(!players.get(f).alive) {
              players.remove(f);
          }
      }
  }



  @Override
  public void render(Engine ge, Renderer r) {

    if (screen.equals("mainMenu")) {
        r.drawImage(backgrounds.get(5), 240, 159, 0);
        for (Star star:starField) {
            star.show(r, ge.getWidth(), ge.getHeight());
            star.update();
        }
        dolls.clear();
        PvE.show(r);
        PvP.show(r);
        Coop.show(r);
        //settings.show(r);
        Exit.show(r);

        sound.show(r);
        music.show(r);
    } else {
        if(!executed3) {
            background = backgrounds.get(Vector.getRandomNumberInRange(0,4));
            executed3 = true;
        }
        r.drawImage(background,240,159,0);
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


      for (Doll wrack: dolls) {
          wrack.show(r);
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
