package visuals;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	protected PVector position;
	protected PVector velocity = new PVector(0,0);
	
	protected float gravity = 0.2f;
	protected float friction = 0.999f;
	protected int alpha = (int)Math.random() * 55 + 200;
	protected boolean isDead = false;
	protected float radius = 2;
	
	public Particle(float x, float y) {
		position = new PVector(x, y);
	}
	
	public Particle(float x, float y, float vx, float vy) {
		position = new PVector(x, y);
		velocity = new PVector(vx, vy);
		
	}
	
	public void render(PApplet app) {
		app.noStroke();
		app.fill(255, 255, 255, alpha);
		
		float mag = velocity.mag();
		//PVector velN = velocity.copy().normalize();
		
		float sx = mag;//velocity.x;//app.map(velocity.x, 0, 10, 1, 10);
		float sy = mag;//velocity.y;//app.map(velocity.y, 0, 10, 1, 10);
		
		app.pushMatrix();
		app.translate(position.x, position.y);
		app.rotate(PApplet.atan2(velocity.y, velocity.x));
		app.ellipse(0,0, sx*2, 1);
		app.popMatrix();
	}
	
	public void randomVelocity() {
		velocity = new PVector((float)(Math.random() - 0.5), (float)(Math.random()-0.5));
		velocity.normalize();
		velocity.mult((float)Math.random() * 5);
	}
	
	public void update() {
		if (isDead) return;
		//velocity.y += gravity;
		velocity.mult(friction);
		position.add(velocity);
		alpha -= 9;
		
		if (alpha <= 0) {
			isDead = true;
		}
	}
	
	public void setPosition(float x, float y) {
		position.set(x,y);
	}
	
	public boolean getIsDead() {
		return isDead;
	}
}
