package network;

import processing.core.PApplet;
import processing.core.PVector;

public class Packet {
	
	static float pxPrSecond = 8;
	
	Node from;
	Node to;
	
	private boolean hasSent = false; 
	
	PVector pos = new PVector(0,0);
	PVector dir = new PVector(0,0);
	
	public Packet(Node from, Node to) {
		this.from = from;
		this.to = to;
		
		this.pos = from.getPosition();
		
		if (((MusicNode)to).getInstantSend()) {
			arrive();
		}
	}
	
	public void update(double dt) {
		if (!hasSent) {
			dir = to.getPosition().sub(pos).normalize();
			pos.add(dir.copy().mult((float) dt));
			
			if (pos.dist(to.getPosition()) < 1) {
				arrive();
			}
		}
	}
	
	public void arrive() {
		to.receivePacket(this);
		hasSent = true;
	}
	
	public void render(PApplet app) {
		app.strokeWeight(3);
		app.stroke(200, 200, 200, 100);
		app.line(pos.x, pos.y, pos.x+dir.x*4, pos.y+dir.y*4);
	}
	
	public boolean getHasSent() {
		return hasSent;
	}
	
	public Node getSender() {
		return from;
	}

}
