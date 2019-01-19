package com.efo.engine;

import java.awt.image.DataBufferInt;

public class Renderer {
  private int pW, pH; //pixle width and height
  int[] p;

  public Renderer(Engine ge) {
    pW = ge.getWidth();
    pH = ge.getHeight();
    p = ((DataBufferInt)ge.getWindow().getImage().getRaster().getDataBuffer()).getData(); //When content of p is Changed, "image" in Window will change accordingly
  }

  public void clear() {
    for(int i = 0; i < p.length; i++) {
      p[i] += 0;
    }
  }
}
