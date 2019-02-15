package com.efo.engine;

import com.efo.engine.gfx.Image;

import java.util.Arrays;

public class Button {
    private String state, name;
    private Image noHover, hover, clicked;
    private int offX, offY, width, height;
    private String [] states;

    public Button(int offX, int offY, String name) {
        state = "noHover";
        states = new String[2];
        Arrays.fill(states, "noHover");
        this.name = name;
        noHover = new Image("/noHover.png");
        clicked = new Image("/clicked.png");
        hover = new Image("/hover.png");
        this.offX = offX;
        this.offY = offY;
        width = noHover.getW();
        height = noHover.getH();
    }

    public Button(int offX, int offY, String name, String hoverPath, String noHoverPath, String clickedPath) {
        state = "noHover";
        states = new String[2];
        Arrays.fill(states, "noHover");
        this.name = name;
        noHover = new Image(noHoverPath);
        clicked = new Image(clickedPath);
        hover = new Image(hoverPath);
        this.offX = offX;
        this.offY = offY;
        width = noHover.getW();
        height = noHover.getH();
    }

    public void update(Vector[] objects, boolean[] tests){
        // Eingaben passen nicht
        if (objects.length != tests.length /*|| objects.length != 2*/){
            System.out.println("Da lief was falsch in Update Button");
            return;
        }

        for (int i = 0; i < objects.length; i++) {
            // hover oder nicht hover
            if(objects[i].getX() < (this.offX + width/2) &&
                objects[i].getX() > (this.offX - width/2) &&
                objects[i].getY() < (this.offY + height/2) &&
                objects[i].getY() > (this.offY - height/2)
            ){
                // clicked oder nicht clicked
                if(tests [i]){
                    if(this.states[i].equals("draggedAway")){
                        states[i] = "clicked";
                    } else{
                        if (this.states[i].equals("movedIn") || this.states[i].equals("noHover")) {
                            states[i] = "movedIn";
                        } else {
                            states[i] = "clicked";
                        }
                    }
                } else{
                    if (this.states[i].equals("clicked")){
                        states[i] = "released";
                    } else {
                        states[i] = "hover";
                    }
                }
            } else{
                if(tests [i]) {
                    if (this.states[i].equals("clicked") || this.states[i].equals("draggedAway")) {
                        states[i] = "draggedAway";
                    } else {
                        states[i] = "noHover";
                    }
                } else{
                    states[i] = "noHover";
                }
            }
        }
        if (Arrays.asList(states).contains("clicked")){
            state = "clicked";
        } else if (Arrays.asList(states).contains("released")){
            state = "released";
        } else if (Arrays.asList(states).contains("movedIn")){
            state = "movedIn";
        } else if (Arrays.asList(states).contains("hover")){
            state = "hover";
        } else if (Arrays.asList(states).contains("draggedAway")){
            state = "draggedAway";
        } else{
            state = "noHover";
        }
    }

    public void show(Renderer r){
        switch (state){
            case "hover": case "released": case "movedIn":
                r.drawImage(hover, offX, offY, 0);
                r.drawText(name, offX - width/2 + 20, offY - height/2 + 13, 0x8f858af2);
                break;
            case "clicked":
                r.drawImage(clicked, offX, offY + 1, 0);
                r.drawText(name, offX - width/2 + 20, offY - height/2 + 14, 0x6F858af2);
                break;
            case "noHover": case "draggedAway":
                r.drawImage(noHover, offX, offY, 0);
                r.drawText(name, offX - width/2 + 20, offY - height/2 + 13, 0xFF858af2);
                break;
            default:
                System.out.println("In Button lief was falsch");
                return;
        }
    }

    public boolean testAction(){
        if (state.equals("released")){
            return true;
        } else{
            return false;
        }
    }
}
