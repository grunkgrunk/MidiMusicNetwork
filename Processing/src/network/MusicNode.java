package network;

import themidibus.MidiBus;

public class MusicNode extends Node {
	private int velocity = 100;
	private int pitch = 64;
	private int channel = 0;
	//private int sendSpeed = 5;
	
	private MidiBus bus;
	
	public MusicNode(MidiBus bus, float x, float y) {
		super(x, y);
		this.bus = bus;
	}
	
	private void sendNoteOn() {
		bus.sendNoteOn(channel, pitch, velocity);
	}
	
	private void sendNoteOff() {
		bus.sendNoteOff(channel, pitch, velocity);
	}
	
	public void sendPackets() {
		generatePackets();
		sendNoteOn();
		
	}
	
	public void receivePacket(Packet packet) {
		sendPackets();
		((MusicNode) packet.getSender()).sendNoteOff();
	}
	
	public void incrementPitch(int delta) {
		pitch += delta; 
		if (pitch < 0) {
			pitch = 0;
		}
		
		if (pitch > 127) {
			pitch = 127;
		}
	}
	
	public void incrementVelocity(int delta) {
		velocity += delta; 
		if (velocity < 0) {
			velocity = 0;
		}
		
		if (velocity > 127) {
			velocity = 127;
		}
	}
	

}
