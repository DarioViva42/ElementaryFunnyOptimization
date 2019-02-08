package com.efo.game;

import com.efo.engine.Renderer;
import com.efo.engine.Vector;
import com.efo.engine.gfx.Image;

public class Projectile {
	private Vector pos;
	private Vector vel;
	private Image munition;

	public Projectile(Vector pos, Vector vel) {
		this.pos = pos;
		this.vel = vel;
		this.munition = new Image("/Munition.png");
	}

	public void show(Renderer r){
		r.drawImage(munition, (int)this.pos.getX(), (int)this.pos.getY(), Math.toRadians(this.vel.getAngle()));
	}

	public void update() {
		this.pos.add(this.vel);
	}

	public Vector getPos() {
		return pos;
	}
}