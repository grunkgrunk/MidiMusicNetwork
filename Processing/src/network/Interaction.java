package network;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

import themidibus.MidiBus;

public class Interaction {
	private static final int LEFT = PConstants.LEFT;
	private static final int RIGHT = PConstants.RIGHT;
	private static final int CENTER = PConstants.CENTER;

	// Two nodes for drag and drop connections between nodes. 
	private Node selectFrom;
	private Node selectTo;
	// The node that is under the cursor
	private Node hitNode;
	
	private float mouseX;
	private float mouseY;
	
	private ArrayList<Node> nodes;
	private MidiBus bus;
	private Grid grid;
	
	public Interaction(ArrayList<Node> nodes, MidiBus bus, Grid grid) {
		this.nodes = nodes;
		this.bus = bus;
		this.grid = grid;
	}
	
	public void render(PApplet app) {
		if (selectFrom != null) {
			PVector pos1 = selectFrom.getPosition();
			PVector pos2 = new PVector(mouseX, mouseY);
			if (selectTo != null) {
				pos2 = selectTo.getPosition();
			}
			app.stroke(255);
			app.strokeWeight(1);
			app.line(pos1.x,pos1.y, pos2.x,pos2.y);
		}
		
		if (hitNode != null) {
			hitNode.renderHover(app);
		} else if (selectFrom != null) {
			selectFrom.renderSelect(app);
		}
	}
	
	
	public void mouseDragged() {
		if (hitNode != selectFrom) {
			selectTo = hitNode;
		} else {
			selectTo = null;
		}
	}
	
	public void mouseWheel(MouseEvent e) {
		if (hitNode != null) {
			((MusicNode) hitNode).incrementPitch(e.getCount());
		}
	}
	
	public void mousePressed(int mouseButton) {
		// This method does an awful lot of things
		if (mouseButton == LEFT) {
			if (hitNode == null) {
				PVector pos = grid.worldToGrid(mouseX, mouseY);
				Node created = new MusicNode(bus, pos.x, pos.y);
				nodes.add(created);
			} else {
				hitNode.sendPackets();
			}
		} else if (mouseButton == RIGHT && hitNode != null) {
			if (selectFrom == null) {
				selectFrom = hitNode;
			}
		} else if (mouseButton == CENTER && hitNode != null) {
			hitNode.clearPackets();
			for (Node n : nodes) {
				n.removeConnection(hitNode);
				n.clearPacketsTo(hitNode);
			}
			
			int index = nodes.indexOf(hitNode);
			nodes.remove(index);
		}

	}

	public void mouseReleased(int mouseButton) {
		if (mouseButton == RIGHT) {
			if (selectFrom != null && selectTo != null) {
				selectFrom.connectTo(selectTo);
			}
		}
		selectFrom = null;
		selectTo = null;
	}

	public void keyPressed(char key) {
		// Restart everything
		if (key == 'r') {
			nodes.clear();
		}
		
	}
	
	// All this would not be needed if we had nodes stored in hashmap
	boolean collisionPointCircle(float x1, float y1, float x2, float y2, float r) {
		double d = dist(x1, y1, x2, y2);
		return d < r;
	}
	
	double dist(float x1, float y1, float x2, float y2) {
		float offx = x2 - x1;
		float offy = y2 - y1;
		
		return Math.sqrt(offx*offx + offy*offy);
	}

	Node collisionNodePosition(float x, float y) {
		for (Node n : nodes) {
			PVector pos = n.getPosition();
			PVector testpos = grid.worldToGrid(x,y);
			if (collisionPointCircle(testpos.x, testpos.y, pos.x, pos.y, n.getRadius())) {
				return n;
			}
		}
		return null;
	}
	
	
	public Node getHitNode() {
		return hitNode;
	}
	
	public void updateHitNode(float mx, float my) {
		mouseX = mx;
		mouseY = my;
		hitNode = collisionNodePosition(mx,my);
	}
}
