package network;

import processing.core.PVector;

public abstract class Circle {
	protected PVector position;
	protected float radius;
	
	protected Circle(float x, float y, float r) {
		position = new PVector(x,y);
		radius = r;
	}
	
	public PVector getPosition() {
		// return a copy of the position
		// so it wont get modified
		return position.copy();
	}
	
	public void setPosition(PVector position) {
		this.position = position;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
