package com.efo.engine;

import com.efo.engine.gfx.Font;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;

public class Renderer {
  private int pW, pH; //pixel width and height
  int[] p;

  private Font font = Font.STANDARD;

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
                                      //Color
  public void setPixel(int x, int y, int value) {
    if((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff) {
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

  public void rectangle(int offX, int offY, int width, int height, int color) {

    for (int y = 0; y <= height; y++) {
      setPixel(offX, offY + y, color);
      setPixel(offX + width, offY + y, color);
    }

    for (int x = 0; x <= width; x++) {
      setPixel(offX + x, offY, color);
      setPixel(offX + x, offY + height, color);
    }

  }

  public void filledRectangle(int offX, int offY, int width, int height, int color) {

    //Dont Render
    if(offX < -width) return;
    if(offY < -height) return;
    if(offX >= pW) return;
    if(offY >= pW) return;

    int newX = 0;
    int newY = 0;
    int newWidth = width;
    int newHeight = height;

    //Clipping
    if(offX < 0) {newX -= offX;}
    if(offY < 0) {newY -= offY;}
    if(newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW);}
    if(newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

    for (int y = 0; y <= newHeight; y++) {
      for (int x = 0; x <= newWidth; x++) {
        setPixel(offX + x, offY + y, color);
      }
    }




  }


}
