package com.efo.engine;

import com.efo.engine.Engine;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class Window {

  private JFrame frame;
  private BufferedImage image; //Stores Pixel data
  private Canvas canvas;
  private BufferStrategy bs;
  private Graphics g;

  public Window(Engine ge){
    image = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_RGB); // Buffered means stored in RAM
    canvas = new Canvas();
    Dimension s = new Dimension((int)(ge.getWidth()*ge.getScale()),(int)(ge.getHeight()*ge.getScale()));
    canvas.setPreferredSize(s);
    canvas.setMaximumSize(s);
    canvas.setMinimumSize(s);

    frame = new JFrame(ge.getTitle());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Always do this after a new JFrame
    frame.setLayout(new BorderLayout());
    frame.add(canvas, BorderLayout.CENTER);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);

    canvas.createBufferStrategy(2);

    bs = canvas.getBufferStrategy();
    g = bs.getDrawGraphics();







  }

  public void update() {
    g.drawImage(image, 0, 0, canvas.getWidth(),canvas.getHeight(),null);
    bs.show();
  }

  public BufferedImage getImage() {
    return image;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public JFrame getFrame() {
    return frame;
  }
}
