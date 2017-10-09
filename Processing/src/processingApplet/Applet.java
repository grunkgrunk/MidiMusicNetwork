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
	ArrayList<Node> nodes = new ArrayList<Node>();
	Interaction interaction;
	Grid grid;
	
	float bpm = 120;
	float prevTime = 0;
	float dt = 0;
	
	//Maybe a hashmap should be used since we have everything in a grid
	//HashMap<PVector, Node> map = new HashMap<PVector, Node>();
	public static void main(String[] args) {
		PApplet.main("processingApplet.Applet");
	}
	
	public void settings() {
		int unit = 64;
		grid = new Grid(unit*10, unit*10, 64);
		size(unit*10, unit*10);
		
	}

	public void setup() {
		prevTime = 0;
		interaction = new Interaction(nodes, bus, grid);
	}

	public void draw() {
		// convert delta time to seconds.
		dt = (millis() - prevTime)/1000;
		
		interaction.updateHitNode(mouseX, mouseY);
		
		for (Node n : nodes) {
			// multiply dt and bpm to get the correct tempo.
			// this is probably not the correct math yet
			// we need to also take the grid into account. 
			// one grid should be equal to 1 eight note.
			
			
			n.update(dt*bpm*grid.getPxPrUnit() / 16);
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
