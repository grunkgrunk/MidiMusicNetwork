package network;

import java.util.ArrayList;
import java.util.List;

import instrument.MidiManager;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Interaction {
	// Should be able to copy paste
	
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
	private MidiManager midiManager;
	private Grid grid;
	private PApplet app;

	private boolean isPaused = false;
	private boolean deleteMode = false;
	private boolean shiftPressed = false;
	
	//private ArrayList<MusicNode> bindings = new ArrayList<MusicNode>();
	
	private char[] keysToBind = {'a','s','d','f', 'g'};
	//private ArrayList<MusicNode> bindings = new ArrayList<MusicNode>(keysToBind.length);
	private List<List<MusicNode>> bindings = new ArrayList<List<MusicNode>>(); 
	

	public Interaction(ArrayList<Node> nodes, MidiManager midiManager, Grid grid, PApplet app) {
		this.nodes = nodes;
		this.midiManager = midiManager;
		this.grid = grid;
		this.app = app;
//		
//		for (int i = 0; i < 19; i++) {
//			PVector pos = grid.gridToWorld(i + 1, 1);
//			Node created = new MusicNode(midiManager, pos.x, pos.y);
//			((MusicNode)created).incrementPitch(i);
//			nodes.add(created);
//		}
		
		for (int i = 0; i < keysToBind.length; i++) {
			bindings.add(new ArrayList<MusicNode>());
		}
	}

	public void render(PApplet app) {
		for (int i = 0; i < bindings.size(); i++) {
			List<MusicNode> bound = bindings.get(i);
			for (MusicNode n : bound) { 
				PVector pos = n.getPosition();
				app.text(keysToBind[i], pos.x, pos.y + 8);
			}
		}
		
		
		if (selectFrom != null) {
			PVector pos1 = selectFrom.getPosition();
			PVector pos2 = new PVector(mouseX, mouseY);
			if (selectTo != null) {
				pos2 = selectTo.getPosition();
			}
			app.stroke(255);
			app.strokeWeight(1);
			app.line(pos1.x, pos1.y, pos2.x, pos2.y);
		}

		if (hitNode != null) {
			hitNode.renderHover(app);
		} else if (selectFrom != null) {
			selectFrom.renderSelect(app);
		}
		
		app.stroke(255, 255, 255, 100);
		if (shiftPressed) {
			app.stroke(255, 0, 0, 100);
		}
		app.strokeWeight(2);
		app.noFill();
		app.ellipse(mouseX, mouseY, 10, 10);
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
				Node created = new MusicNode(midiManager, pos.x, pos.y);
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

	public void keyPressed(char key, int keyCode) {
		if (key == PConstants.CODED) {
			if (keyCode == PConstants.SHIFT) {
				shiftPressed = true;
			}
		}
		
		if (key ==  ' ') {
			midiManager.setSustain(true);
		}
		
		
		if (key == 'c') {
			nodes.clear();
			for (List<MusicNode> bound : bindings) {
				bound.clear();
			}
			
		}
		
		if (key == 'i') {
			if (hitNode != null) {
				MusicNode n = (MusicNode)hitNode;
				n.setInstantSend(!n.getInstantSend());
			}
		}
		
		for (int i = 0; i < keysToBind.length; i++) {
			
			char k = keysToBind[i];
			if (k == Character.toLowerCase(key)) {
				List<MusicNode> toPlay = bindings.get(i);
				if (hitNode != null && shiftPressed) {
					if (!toPlay.contains((MusicNode)hitNode)) {
						toPlay.add((MusicNode)hitNode);
					} else {
						toPlay.remove((MusicNode)hitNode);
					}
				} else {
					for (MusicNode n : toPlay) {
						n.sendPackets();
					}
				}
			}
		}
	}
	
	public void keyReleased(char key, int keyCode) {
		if (key == PConstants.CODED) {
			if (keyCode == PConstants.SHIFT) {
				shiftPressed = false;
			}
		}
		
		if (key ==  ' ') {
			midiManager.setSustain(false);
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

		return Math.sqrt(offx * offx + offy * offy);
	}

	Node collisionNodePosition(float x, float y) {
		for (Node n : nodes) {
			PVector pos = n.getPosition();
			PVector testpos = grid.worldToGrid(x, y);
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
		hitNode = collisionNodePosition(mx, my);
	}

	
}
