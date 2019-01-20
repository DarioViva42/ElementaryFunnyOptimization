package com.efo.engine;

import com.efo.engine.gfx.Image;
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
    for(int y = 0; y < image.getH(); y++) {
      for(int x = 0; x < image.getW(); x++) {
        setPixel(x + offX,y + offY, image.getP()[x+y*image.getW()]);
      }
    }
  }
}
