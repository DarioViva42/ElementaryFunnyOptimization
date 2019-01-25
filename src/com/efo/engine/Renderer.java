package com.efo.engine;

import com.efo.engine.gfx.Font;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.Color;
import java.awt.image.DataBufferInt;

public class Renderer {
  private int pW, pH; //pixel width and height
  int[] p;

  Window win;

  private Font font = Font.STANDARD;

  public Renderer(Engine ge) {
    pW = ge.getWidth();
    pH = ge.getHeight();
    p = ((DataBufferInt)ge.getWindow().getImage().getRaster().getDataBuffer()).getData(); //When content of p is Changed, "image" in Window will change accordingly
    win = ge.getWindow();
  }

  public void clear() {
    for(int i = 0; i < p.length; i++) {
      p[i] = 0x00000000;
    }
  }
                                      //Color
  public void setPixel(int x, int y, int value) {
    if((x < 0 || x >= pW || y < 0 || y >= pH) || ((value >> 24) & 0xff) == 0) {
      return;
    }

    //Converting 2d Number to 1d Array
    p[x + y *pW] = value;
  }

  public void drawText(String text, int offX, int offY, int color){
    text = text.toUpperCase();
    int offset = 0;

    for (int i = 0; i < text.length(); i++) {
      int unicode = text.codePointAt(i) - 32;

      for (int y = 0; y < font.getFontImage().getH(); y++) {
        for (int x = 0; x < font.getWidths()[unicode]; x++) {
          if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff){
            setPixel(x + offX + offset, y + offY, color);
          }
        }
      }
      offset += font.getWidths()[unicode];
    }
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
        setPixel(x + offX,y + offY, image.getP()[(x+tileX*image.getTileW()) + (y+tileY*image.getTileH()) * image.getW()]);
      }
    }
  }

  public void oval(int x, int y, int width, int height) {
    win.getG().drawOval(x,y,width, height);
  }

  public void rectangle(int x, int y, int width, int height) {
    win.getG().drawRect(x,y,width,height);
  }

  public void filledOval(int x, int y, int width, int height) {
    win.getG().fillOval(x,y,width,height);
  }

  public void filledRectangle(int x, int y, int width, int height) {
    win.getG().fillRect(x,y,width,height);
  }

  public void line(int x1, int y1, int x2, int y2) {
    win.getG().drawLine(x1,y1,x2,y2);
  }

}
