package com.efo.engine;

import com.efo.engine.gfx.Font;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.Color;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Renderer {
  private int pW, pH; //pixel width and height
  int[] p;
  private final int[][] kern = {{1,1,1},{1,1,1},{1,1,1}};
  private final int kSum = 9;

  private Font font = Font.STANDARD;

  public Renderer(Engine ge) {
    pW = ge.getWidth();
    pH = ge.getHeight();
    p = ((DataBufferInt)ge.getWindow().getImage().getRaster().getDataBuffer()).getData(); //When content of p is Changed, "image" in Window will change accordingly

  }

  public void clear() {
    for(int i = 0; i < p.length; i++) {
      p[i] = 0xff000000;
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

  public void drawImage(Image image, int offX, int offY, double angle) {
    int tX, tY, rX, rY;
    int w = image.getW();
    int h = image.getH();

    for(int y = 0;  y < h; y++) {
      for(int x = 0; x < w; x++) {
        tX = x - w / 2;
        tY = y - h / 2;

        rX =(int)(tX * Math.cos(angle) - tY * Math.sin(angle));
        rY =(int)(tX * Math.sin(angle) + tY * Math.cos(angle));
        setPixel(rX + offX,rY + offY, image.getP()[x+y*image.getW()]);
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


  public void circle(int offX, int offY, int r, int color) {

    double i, angle, x1, y1, pi;
    pi = Math.PI;

    for (i = 0; i <= 360; i += 0.1) {
      angle = i;
      x1 = r * Math.cos(angle * pi / 180);
      y1 = r * Math.sin(angle * pi / 180);
      setPixel((int) (offX + x1), (int) (offY + y1), color);
    }
  }


  public void antiAliasing() {
             //a r g b
    int[] argb = {0,0,0,0};

    int[] pCopy = new int[pW*pH];

    for (int x = 0; x < pW; x++) {
      for (int y = 0; y < pH; y++) {
        Arrays.fill(argb, 0);

        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {

            try {
              int rgb = p[(x + i - 1) + (y + j - 1) * pW];

              //if(rgb != 0xff000000) {
                int alpha = rgb / 0x01000000;
                int red = rgb / 0x00010000;
                int green = rgb / 0x00000100;
                int blue = rgb / 0x00000001;

                argb[0] += kern[i-1][j-1] * alpha;
                argb[1] += kern[i-1][j-1] * red;
                argb[2] += kern[i-1][j-1] * green;
                argb[3] += kern[i-1][j-1] * blue;
              //}
            } catch (IndexOutOfBoundsException e) {

            }
          }
        }

        for (int i = 0; i < 4; i++) {
          argb[i] = argb[i] / kSum;
        }

        int color = argb[0] * 0x01000000 + argb[1] * 0x00010000 + argb[2] * 0x00000100 + argb[3] * 0x00000001;

        pCopy[x+y*pW] = color;

      }
    }


    for (int x = 0; x < pW; x++) {
      for (int y = 0; y < pH; y++) {
        setPixel(x,y,pCopy[x+y*pW]);
      }
    }
  }





























  public void antiAliasing2() {
    int pixel, pixelP, upper, upperP, lower, lowerP, left, leftP, right, rightP;

    for (int x = 0; x < pW; x++) {
      for (int y = 0; y < pH; y++) {

        pixelP = x + y * pW;
        upperP = x + (y - 1) * pW;
        lowerP = x + (y + 1) * pW;
        rightP = (x + 1) + y * pW;
        leftP = (x - 1) + y * pW;


        pixel = p[pixelP];

        if(pixel != 0xff000000) {
          if (upperP >= 0) {
            upper = p[upperP];
            upper = upper - 12 * (0x01000000);

            setPixel(x, y, upper);
          }

          if (lowerP < pH * pW) {
            lower = p[lowerP];

            lower = lower - 12 * (0x02000000);
            setPixel(x, y, lower);
          }

          if (!(rightP % pW == 0)) {
            right = p[rightP];
            right = right - 12 * (0x02000000);
            setPixel(x, y + 1, right);
          }

          if (leftP % pW == (pW)) {
            left = p[leftP];
            left = left - 12 * (0x01000000);
            setPixel(x, y, left);
          }

        }

      }
    }

    //p[x + y *pW] = value;


  }

}
