package com.efo.engine;

import com.efo.engine.gfx.Image;

public class Checkbox {
	private Button unTicked, ticked;
	private int offX, offY;
	private boolean state, action;
	private Image icon;

	public Checkbox(int offX, int offY, Image icon) {
		this.offX = offX;
		this.offY = offY;
		this.icon = icon;
		state = true;
		this.unTicked = new Button(offX,offY,"", "/checkbox/untickedHover.png","/checkbox/untickedNoHover.png","/checkbox/untickedClicked.png");
		this.ticked = new Button(offX,offY,"", "/checkbox/tickedHover.png","/checkbox/tickedNoHover.png","/checkbox/tickedClicked.png");
	}

	public void update(Vector[] objects, boolean[] tests, boolean sound) {
		unTicked.update(objects, tests, sound);
		ticked.update(objects, tests, false);
		if (unTicked.testAction()) {
			action = true;
			state = !state;
		} else {
			action = false;
		}
	}

	public void show(Renderer r){
		r.drawImage(icon, offX-50, offY, 0);
		if (state){
			ticked.show(r);
		} else {
			unTicked.show(r);
		}
	}

	public boolean testState(){
		return state;
	}
	public boolean testAction(){
		return action;
	}

}
