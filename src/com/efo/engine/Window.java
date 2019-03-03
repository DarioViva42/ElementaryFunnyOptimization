//all of this code is from Majoolwhips 2D Engine tutorial

package com.efo.engine;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window {

  private BufferedImage image; //Stores Pixel data
  private Canvas canvas;
  private BufferStrategy bs;
  private Graphics g;

  Window(Engine ge){

    JFrame frame;
    image = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB); // Buffered means stored in RAM
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
    frame.toFront();
    frame.requestFocus();

    canvas.createBufferStrategy(2);
    canvas.requestFocusInWindow();

    bs = canvas.getBufferStrategy();
    g = bs.getDrawGraphics();


    // Vielleicht finden wir das noch raus wie man ein Bild direkt anwählen kann.
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image image = toolkit.getImage("/blu.png");
    Cursor c = toolkit.createCustomCursor(image , new Point(frame.getX(),
            frame.getY()), "img");
    frame.setCursor (c);


  }

  public void update() {
    g.drawImage(image, 0, 0, canvas.getWidth(),canvas.getHeight(),null);
    bs.show();
  }

  public BufferedImage getImage() {
    return image;
  }

  Canvas getCanvas() {
    return canvas;
  }
}
