package com.efo.engine;

import com.efo.engine.gfx.Image;

public class Checkbox {
	private Button unticked, ticked;
	private int offX, offY, width, height;
	private boolean state;
	private Image icon;

	public Checkbox(int offX, int offY, Image icon) {
		this.offX = offX;
		this.offY = offY;
		this.icon = icon;
		state = true;
		this.unticked = new Button(offX,offY,"", "/CB_unticked-hover.png","/CB_unticked-noHover.png","/CB_unticked-clicked.png");
		this.ticked = new Button(offX,offY,"", "/CB_ticked-hover.png","/CB_ticked-noHover.png","/CB_ticked-clicked.png");
	}

	public void update(Vector[] objects, boolean[] tests) {
		unticked.update(objects, tests);
		ticked.update(objects, tests);
		if (unticked.testAction()) {
			if(state){
				state = false;
			} else {
				state = true;
			}
		}
	}

	public void show(Renderer r){
		r.drawImage(icon, offX-50, offY, 0);
		if (state){
			ticked.show(r);
		} else {
			unticked.show(r);
		}
	}

	public boolean testState(){
		return state;
	}

}
