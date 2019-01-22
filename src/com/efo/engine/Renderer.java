package com.efo.engine;

import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;

public class Renderer {
  private int pW, pH; //pixel width and height
  int[] p;

  public Renderer(Engine ge) {
    pW = ge.getWidth();
    pH = ge.getHeight();
    p = ((DataBufferInt)ge.getWindow().getImage().getRaster().getDataBuffer()).getData(); //When content of p is Changed, "image" in Window will change accordingly
  }

  public void clear() {
    for(int i = 0; i < p.length; i++) {
      p[i] = 0;
    }
  }

  public void setPixel(int x, int y, int value) {
    if((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff) {
      return;
    }

    //Converting 2d Number to 1d Array
    p[x + y *pW] = value;
  }

  public void drawImage(Image image, int offX, int offY ) {
    //Dont Render
    if(offX < -image.getW()) return;
    if(offY < -image.getH()) return;
    if(offX >= pW) return;
    if(offY >= pW) return;

    int newX = 0;
    int newY = 0;
    int newWidth = image.getW();
    int newHeight = image.getH();

    //Clipping
    if(offX < 0) {newX -= offX;}
    if(offY < 0) {newY -= offY;}
    if(newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW);}
    if(newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

    for(int y = newY;  y < newHeight; y++) {
      for(int x = newX; x < newWidth; x++) {
        setPixel(x + offX,y + offY, image.getP()[x+y*image.getW()]);
      }
    }
  }

  public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY){
    //Dont Render
    if(offX < -image.getTileW()) return;
    if(offY < -image.getTileH()) return;
    if(offX >= pW) return;
    if(offY >= pW) return;

    int newX = 0;
    int newY = 0;
    int newWidth = image.getTileW();
    int newHeight = image.getTileH();

    //Clipping
    if(offX < 0) {newX -= offX;}
    if(offY < 0) {newY -= offY;}
    if(newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW);}
    if(newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

    for(int y = newY;  y < newHeight; y++) {
      for(int x = newX; x < newWidth; x++) {
        setPixel(x + offX,y + offY, image.getP()[x+y*image.getTileW()]);
      }
    }
  }
}
