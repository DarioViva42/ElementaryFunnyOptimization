//all of this code is from Majoolwhips 2D Engine tutorial

package com.efo.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

  private Engine ge;

  private final int NUM_KEYS = 256;
  private boolean[] keys = new boolean[NUM_KEYS];
  private boolean[] keysLast = new boolean[NUM_KEYS];


  private final int NUM_BUTTONS = 5;
  private boolean[] buttons = new boolean[NUM_BUTTONS];

  private int mouseX, mouseY;
  private int scroll;

  public Input(Engine ge) {
    this.ge = ge;
    mouseX = 0;
    mouseY = 0;
    scroll = 0;

    //add listeners to canvas
    ge.getWindow().getCanvas().addKeyListener(this);
    ge.getWindow().getCanvas().addMouseMotionListener(this);
    ge.getWindow().getCanvas().addMouseListener(this);
    ge.getWindow().getCanvas().addMouseWheelListener(this);
  }


  public void update() {

    scroll = 0;

    for (int i = 0; i < NUM_KEYS; i++) {
      keysLast[i] = keys[i];
    }
  }

  public boolean isKey(int keyCode) {
    return keys[keyCode];
  }

  boolean isKeyDown(int keyCode) {
    return keys[keyCode] && !keysLast[keyCode];
  }

  public boolean isButton(int button) {
    return buttons[button];
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    buttons[e.getButton()] = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    buttons[e.getButton()] = false;
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mouseDragged(MouseEvent e) {
    mouseX = (int)((e.getX()) / ge.getScale());
    mouseY = (int)((e.getY()) / ge.getScale());
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    mouseX = (int)((e.getX()) / ge.getScale());
    mouseY = (int)((e.getY()) / ge.getScale());
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    scroll = e.getWheelRotation();
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }
}
