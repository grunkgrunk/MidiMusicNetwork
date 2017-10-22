package network;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public abstract class Node extends Circle {
	
	
	// Nodes are responsible for managing packets it sends to other nodes. 
	private ArrayList<Packet> packets = new ArrayList<Packet>();
	// All the nodes that are connected to this node.
	// Connections this node has to other nodes need not to go the other way around.
	// ex: node1 is connected to node2, but node2 is not connected to node1. 
	private ArrayList<Node> connections = new ArrayList<Node>();
	
	static int maxPackets = 1000;
	
	public Node(float x, float y) {
		// default radius of node is 16 px
		super(x,y,10);
	}
	
	
	protected void generatePackets() {
		for (int i = 0; i < connections.size(); i++) {
			// not the nicest way to limit amount of packages.
			if (packets.size() < maxPackets) {
				Packet p = new Packet(this, connections.get(i));
				packets.add(p);
			} else {
				break;
			}
		}
	}
	
	public void connectTo(Node n) {
		if (!connections.contains(n) && n != this) {
			connections.add(n);
		}
	}
	
	// How we send and receive packets might vary from node to node. 
	public abstract void sendPackets();
	public abstract void receivePacket(Packet packet);
	
	public void removeConnection(Node n) {
		// We remove the connection if it is present.
		int i = connections.indexOf(n);
		if (i != -1) {
			connections.remove(i);
		}
	}
	
	public void update(double dt) {
		// All packets get updated. If the packet has arrived at its destination,
		// it will be removed. 
		for (int i = packets.size()-1; i >= 0; i--) {
			Packet p = packets.get(i);
			p.update(dt);
			if(p.getHasSent()) {
				packets.remove(i);
			}
		}
	}
	
	// Should maybe move render methods into an interface or into the circle-class
	// the render methods could also be abstract methods and be implemented in derived node classes
	public void renderNormal(PApplet app) {
		app.fill(255);
		app.noStroke();
		app.ellipse(position.x, position.y, radius*2, radius*2);
		
		app.textAlign(PConstants.CENTER);
		app.fill(0);
		//app.text(pitch, pos.x, pos.y);
	}
	
	public void renderHover(PApplet app) {
		app.stroke(10);
		app.strokeWeight(3);
		app.noFill();
		
		app.ellipse(position.x, position.y, radius*2, radius*2);
	}
	
	public void renderSelect(PApplet app) {
		app.stroke(30, 30, 30, 200);
		app.strokeWeight(6);
		app.noFill();
		app.ellipse(position.x, position.y, radius*2, radius*2);
	}
	
	public void renderConnections(PApplet app) {
		for (Node n : connections) {
			PVector other = n.getPosition();
			PVector dir = other.copy().sub(position).normalize();
			PVector start = dir.copy().mult(radius).add(position);
			PVector end = dir.copy().mult(-n.getRadius()).add(other);
			
			boolean isInstant = ((MusicNode)n).getInstantSend();
			app.stroke(150);
			if (isInstant) {
				app.stroke(255, 0, 0);	
			}
			
			app.strokeWeight(1);
			app.line(start.x, start.y, end.x, end.y);
			app.fill(150);
			app.ellipse(end.x,end.y, 8, 8);
		}
	}
	
	public void renderPackets(PApplet app) {
		for (Packet p : packets) {
			p.render(app);
		}
	}
	
	public void clearPackets() {
		packets.clear();
	}
	
	// If we want to remove all the packets that are being sent to a 
	// specific node. 
	public void clearPacketsTo(Node n) {
		for (int i = packets.size()-1; i >= 0; i--) {
			Packet p = packets.get(i);
			
			if(p.to == n) {
				packets.remove(i);
			}
		}
	}
}
