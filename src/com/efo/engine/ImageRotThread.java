package com.efo.engine;

import com.efo.engine.gfx.Image;

public class ImageRotThread extends Thread {
	int startY, StripeY, startX, newWidth, w, h, offX, offY;
	double angle;
	Image image;
	Renderer r;

	public ImageRotThread(int startY, int StripeY, int startX, int newWidth, double angle, Image image, int w, int h, int offX, int offY, Renderer r){
		this.startY=startY;
		this.StripeY=StripeY;
		this.startX=startX;
		this.newWidth=newWidth;
		this.angle=angle;
		this.image=image;
		this.w=w;
		this.h=h;
		this.offX=offX;
		this.offY=offY;
		this.r=r;
	}
	public void run(){
		for (int y = startY; y < startY+StripeY; y++) {
			for (int x = startX; x < newWidth; x++) {

				int rX = (int) (x * Math.cos(angle) + y * Math.sin(angle) + w/2.0);
				int rY = (int) (-x * Math.sin(angle) + y * Math.cos(angle) + h/2.0);

				if (rX < w && rX >= 0 && rY < h && rY >= 0) {
					int color = image.getP()[rX + rY * image.getW()];
					r.setPixel(x + offX, y + offY, color);
				}
			}
		}
	}
}
