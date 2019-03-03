//Our intellectual property

package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

public class Projectile {
	private Vector pos;
	private Vector vel;
	private Image laser;

	Projectile(Vector pos, Vector vel, String faction) {
		this.pos = pos;
		this.vel = vel;
		if(faction.equals("rebel")) {
			laser = new Image("/etc/ammunitionRed.png");
		} else if(faction.equals("empire")) {
			laser = new Image("/etc/ammunitionGreen.png");
		}
	}

	public void show(Renderer r){
		r.drawImage(laser, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.vel.getAngle()));
	}

	public void update() {
		this.pos.add(this.vel);
	}

	Vector getPos() {
		return pos;
	}
}