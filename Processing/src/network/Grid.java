package network;

import processing.core.PApplet;
import processing.core.PVector;

public class Grid {
	private float width;
	private float height;
	private float onX;
	private float onY;
	
	private float pxPrUnit;
	
	public Grid(float w, float h, float unit) {
		width = w;
		height = h;
		onX = w/unit;
		onY = h/unit;
		
		pxPrUnit = unit;
	}
	
	public PVector worldToGrid(float x, float y) {
		PVector pos = new PVector(0,0);
		pos.x = Math.round(x/pxPrUnit)*pxPrUnit;
		pos.y = Math.round(y/pxPrUnit)*pxPrUnit;
		
		return pos;
	}
	
	public PVector gridToWorld(float x, float y) {
		PVector pos = new PVector(x,y);
		pos.mult(pxPrUnit);
		
		return pos;
	}
	
	public void render(PApplet app) {
		app.stroke(45);
		app.strokeWeight(1);
		for (int y = 0; y < onY; y++) {
			app.line(0, y*pxPrUnit, width, y*pxPrUnit);
		}
		for (int x = 0; x < onX; x++) {
			app.line(x*pxPrUnit, 0, x*pxPrUnit, height);
		}
	}
}
