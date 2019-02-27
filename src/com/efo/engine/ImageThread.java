package com.efo.engine;

import com.efo.engine.gfx.Image;

public class ImageThread extends Thread {
	int startY, stripeY, startX, newWidth, w, h, offX, offY;
	Image image;
	Renderer r;

	public ImageThread(int startY, int stripeY, int startX, int newWidth, Image image, int w, int h, int offX, int offY, Renderer r){
		this.startY=startY;
		this.stripeY=stripeY;
		this.startX=startX;
		this.newWidth=newWidth;
		this.image=image;
		this.w=w;
		this.h=h;
		this.offX=offX;
		this.offY=offY;
		this.r=r;
	}
	public void run(){
		for (int y = startY; y < startY + stripeY; y++) {
			for (int x = startX; x < newWidth; x++) {
				int color = image.getP()[x + y * image.getW()];
				r.setPixel(x -w/2 + offX, y - h/2 + offY, color);
			}
		}
	}
}
