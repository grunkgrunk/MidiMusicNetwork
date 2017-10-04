package processingApplet;

import java.util.ArrayList;
//import java.util.HashMap;

import network.Grid;
import network.Interaction;
import network.Node;
import processing.core.PApplet;
import processing.event.MouseEvent;
import themidibus.MidiBus;

public class Applet extends PApplet {

	MidiBus bus = new MidiBus(this, -1, 1);
	float bpm = 120;
	float pxPrStep = 64; 
	
	float prevTime = 0;
	float dt = 0;
	
	//Maybe a hashmap should be used since we have everything in a grid
	//HashMap<PVector, Node> map = new HashMap<PVector, Node>();
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	Interaction interaction;
	Grid grid;
	// Should make a class for the ui behavior.
	// Two nodes for drag and drop connections between nodes. 
	//Node selectFrom;
	//Node selectTo;
	// The node that is under the cursor
	//Node mouseNode;

	public static void main(String[] args) {
		PApplet.main("processingApplet.Applet");
		
	}
	
	public void settings() {
		size((int)pxPrStep*10, (int)pxPrStep*10);
	}

	public void setup() {
		prevTime = 0;
		grid = new Grid(width, height, 64);
		interaction = new Interaction(nodes, bus, grid);
	}

	public void draw() {
		// convert delta time to seconds.
		dt = (millis() - prevTime)/1000;
		
		//mouseNode = collisionNodeMouse();
		interaction.updateHitNode(mouseX, mouseY);
		
		for (Node n : nodes) {
			// multiply dt and bpm to get the correct tempo.
			n.update(dt*bpm);
		}
		
		background(50);
		
		// Draw the grid the grid where the nodes can be placed. 
		grid.render(this);
		
		// Draw things in the correct order. 
		for (Node n : nodes) {
			n.renderConnections(this);
		}
		
		for (Node n : nodes) {
			n.renderPackets(this);
		}

		for (Node n : nodes) {
			n.renderNormal(this);
		}
		
		// Draw how the system responds to user input. 
		interaction.render(this);
		
		prevTime = millis();

	}

	public void mouseDragged() {
		interaction.mouseDragged();
	}
	
	public void mouseWheel(MouseEvent e) {
		interaction.mouseWheel(e);
	}
	
	public void mousePressed() {
		interaction.mousePressed(mouseButton);

	}

	public void mouseReleased() {
		interaction.mouseReleased(mouseButton);
	}

	public void keyPressed() {
		interaction.keyPressed(key);
		
	}
}
