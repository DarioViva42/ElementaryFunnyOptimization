//a lot of this code is from Majoolwhips 2D Engine tutorial. But we've made some major improvements with
//introducing transparency or making it possible to turn pictures seemlessly

package com.efo.engine;

import com.efo.engine.gfx.Font;
import com.efo.engine.gfx.Image;
import com.efo.engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Renderer {
	private int pW, pH; //pixel width and height
	private  int[] p;
	private final int[][] kern = {{1,4,1},{4,32,4},{1,4,1}};
	private int kSum = 0;

  private Font font = Font.STANDARD;

  public Renderer(Engine ge) {
    pW = ge.getWidth();
    pH = ge.getHeight();
    p = ((DataBufferInt)ge.getWindow().getImage().getRaster().getDataBuffer()).getData(); //When content of p is Changed, "image" in Window will change accordingly

    //Adding kern Sum to divide by later
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        kSum += kern[i][j];
      }
    }
  }
	void clear() {
    for(int i = 0; i < p.length; i++) {
      p[i] = 0xff000000;
    }
  }
                                      //Color
  public void setPixel(int x, int y, int value) {
    if((x < 0 || x >= pW || y < 0 || y >= pH) || ((value >> 24) & 0xff) == 0) {
      return;
    }

    // Teiltransparent
    if(((value >> 24) & 0xff) != 255) {
      p[x + y *pW] = blend(p[x + y *pW], value, (float)((value >> 24) & 0xff) / 255);
      return;
    }

    //Converting 2d Number to 1d Array
    p[x + y *pW] = value;

  }

	void drawText(String text, int offX, int offY, int color){

    // Da folgende Zeichen nicht in unserem Zeichensatz enthalten sind ersetzen wir sie.
    text = text.replaceAll("ä", "ae");
    text = text.replaceAll("Ä", "Ae");
    text = text.replaceAll("ö", "oe");
    text = text.replaceAll("Ö", "Oe");
    text = text.replaceAll("ü", "ue");
    text = text.replaceAll("Ü", "Ue");

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
    int w = image.getW();
    int h = image.getH();

	  if (angle != 0) {

		  int berX = (int)((Math.abs(w * Math.cos(angle)) + Math.abs(h * Math.sin(angle)))/2)+2;
		  int berY = (int)((Math.abs(w * Math.sin(angle)) + Math.abs(h * Math.cos(angle)))/2)+2;

		  //Dont Render
		  if(offX < -berX) return;
		  if(offY < -berY) return;
		  if(offX - berX >= pW) return;
		  if(offY - berY >= pH) return;

		  int newX = -berX;
		  int newY = -berY;
		  int newWidth = berX;
		  int newHeight = berY;

		  //Clipping
		  if(offX + newX < 0) {newX -= offX + newX;}
		  if(offY + newY < 0) {newY -= offY + newY;}
		  if(newWidth + offX >= pW) {newWidth -= (newWidth + offX - pW);}
		  if(newHeight + offY >= pH) {newHeight -= (newHeight + offY - pH);}

          for (int y = newY; y < newHeight; y++) {
              for (int x = newX; x < newWidth; x++) {

                  int rX = (int) (x * Math.cos(angle) + y * Math.sin(angle) + w/2.0);
                  int rY = (int) (-x * Math.sin(angle) + y * Math.cos(angle) + h/2.0);

                  if (rX < w && rX >= 0 && rY < h && rY >= 0) {
                      int color = image.getP()[rX + rY * image.getW()];
                      setPixel(x + offX, y + offY, color);
                  }
              }
          }
	  } else{

		  //Dont Render
		  if(offX < -w/2) return;
		  if(offY < -h/2) return;
		  if(offX - w/2 >= pW) return;
		  if(offY - h/2 >= pH) return;

		  int newX = 0;
		  int newY = 0;
		  int newWidth = w;
		  int newHeight = h;

		  //Clipping
		  if(offX < w/2) {newX -= offX - w/2;}
		  if(offY < h/2) {newY -= offY - h/2;}
		  if(newWidth + offX - w/2 >= pW) {newWidth -= (newWidth + offX - pW - w/2);}
		  if(newHeight + offY - h/2>= pH) {newHeight -= (newHeight + offY - pH - h/2);}


          for (int y = newY; y < newHeight; y++) {
              for (int x = newX; x < newWidth; x++) {
                  int color = image.getP()[x + y * image.getW()];
                  setPixel(x -w/2 + offX, y - h/2 + offY, color);
              }
          }
	  }

  }

  public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY){
    //Dont Render
    if(offX < -image.getTileW()) return;
    if(offY < -image.getTileH()) return;
    if(offX >= pW) return;
    if(offY >= pH) return;

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
        setPixel(x + offX,y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
      }
    }
  }




    //Self made
    void antiAliasing() {
                //a r g b
    int[] argb = {0,0,0,0};

    //We make a Copy of the pixel Array as to be able to work with unprocessed pixels
    int[] pCopy = new int[p.length];

    //going through the pixel Array (p) with x and y values
    for (int x = 0; x < pW; x++) {
      for (int y = 0; y < pH; y++) {
        //Initiating the Color Array with Zeroes
        Arrays.fill(argb, 0);

        //Going through the Kern to get the average Color with the according weights
        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {

            try {
              int rgb = p[x + i + (y + j) * pW];

              int alpha = (rgb >> 24) & 0x0FF;
              int red = (rgb >> 16) & 0x0FF;
              int green = (rgb >> 8) & 0x0FF;
              int blue = rgb & 0xFF;

              //if(i == 0 && j == 0 && red > 5 && green > 5 && blue > 5) {

                argb[0] += kern[i + 1][j + 1] * alpha;
                argb[1] += kern[i + 1][j + 1] * red;
                argb[2] += kern[i + 1][j + 1] * green;
                argb[3] += kern[i + 1][j + 1] * blue;
              //}

            } catch (IndexOutOfBoundsException e) {
              //Do Nothing
            }
          }
        }

        for (int i = 0; i < 4; i++) {
          argb[i] = argb[i] / kSum;
        }

        int color = argb[0] * 0x01000000 + argb[1] * 0x00010000 + argb[2] * 0x00000100 + argb[3];

        pCopy[x+y*pW] = color;

      }
    }

      /*for (int i = 0; i < p.length; i++) {
          p[i] = pCopy[i];
      }*/

      for (int x = 0; x < pW; x++) {
          for (int y = 0; y < pH; y++) {
              setPixel(x,y,pCopy[x+y*pW]);
          }
      }

  }

// Aus einem Forum
private int blend (int a, int b, float ratio) {
    if (ratio > 1f) {
      ratio = 1f;
    } else if (ratio < 0f) {
      ratio = 0f;
    }
    float iRatio = 1.0f - ratio;

    //int aA = (a >> 24 & 0xff);
    int aR = ((a & 0xff0000) >> 16);
    int aG = ((a & 0xff00) >> 8);
    int aB = (a & 0xff);

    //int bA = (b >> 24 & 0xff);
    int bR = ((b & 0xff0000) >> 16);
    int bG = ((b & 0xff00) >> 8);
    int bB = (b & 0xff);

    int aM = 255;
    int rM = (int)((aR * iRatio) + (bR * ratio));
    int gM = (int)((aG * iRatio) + (bG * ratio));
    int bM = (int)((aB * iRatio) + (bB * ratio));

    return aM << 24 | rM << 16 | gM << 8 | bM;
  }

}
