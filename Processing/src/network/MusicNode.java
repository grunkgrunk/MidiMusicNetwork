package network;

import instrument.MidiManager;

public class MusicNode extends Node {
	private int velocity = 100;
	private int pitch = 64;

	//private int sendSpeed = 5;
	
	private MidiManager midiManager;
	
	public MusicNode(MidiManager midiManager, float x, float y) {
		super(x, y);
		this.midiManager = midiManager;
	}
	
	public void sendPackets() {
		generatePackets();
		midiManager.addSignal(pitch, velocity);
	}
	
	public void receivePacket(Packet packet) {
		sendPackets();
		//((MusicNode) packet.getSender()).sendNoteOff();
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
