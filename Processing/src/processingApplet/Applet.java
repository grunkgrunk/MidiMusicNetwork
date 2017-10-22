package processingApplet;

import java.util.ArrayList;
//import java.util.HashMap;

import instrument.MidiManager;
import network.Grid;
import network.Interaction;
import network.Node;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import themidibus.MidiBus;

public class Applet extends PApplet {

	MidiBus bus = new MidiBus(this, -1, 1);
	MidiManager midiManager = new MidiManager(bus);
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
		grid = new Grid(unit*20, unit*20, 32);
		size(unit*10, unit*10);
		
	}

	public void setup() {
		
		PFont myFont = createFont("Arial", 9);
		textFont(myFont);
		
		noCursor();

		prevTime = 0;
		interaction = new Interaction(nodes, midiManager, grid, this);
	}

	public void draw() {
		// convert delta time to seconds.
		dt = (millis() - prevTime)/1000;
		
		midiManager.update(dt);
		
		interaction.updateHitNode(mouseX, mouseY);
		
		for (Node n : nodes) {
			// one grid cell equals one 16'th note.
			n.update((dt/60)*grid.getPxPrUnit()*bpm*2);
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
		
		fill(255, 255, 255);
		text(frameRate, 20,20);
		
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
		interaction.keyPressed(key, keyCode);
		if (key == 'c') {
			midiManager.setSustain(true);
		}
	}
	
	public void keyReleased() {
		interaction.keyReleased(key, keyCode);
	}
}
