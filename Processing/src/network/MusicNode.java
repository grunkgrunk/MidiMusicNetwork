package network;

import instrument.MidiManager;
import processing.core.PApplet;
import processing.core.PConstants;

public class MusicNode extends Node {
	private int scaleIndex = 0;
	private int octaveIndex = 5;
	
	private static final String[] alphabet = 
		{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	
	
	private static final int[] scale = {0, 2, 3, 5, 7, 8, 11};
	
	private int velocity = 100;
	private int pitch = scale[scaleIndex] + octaveIndex * 12;
	//private int sendSpeed = 5;
	private boolean instantSend = false;
	
	private MidiManager midiManager;
	private boolean isMute = false;
	
	public MusicNode(MidiManager midiManager, float x, float y) {
		super(x, y);
		this.midiManager = midiManager;
	}
	
	public MusicNode(MidiManager midiManager, float x, float y, boolean instantSend) {
		super(x, y);
		this.midiManager = midiManager;
		this.instantSend = instantSend;
	}
	
	public void sendPackets() {
		generatePackets();
		if (!isMute) {
			midiManager.addSignal(pitch, velocity);
		}
	}
	
	public void receivePacket(Packet packet) {
		sendPackets();
		//((MusicNode) packet.getSender()).sendNoteOff();
	}
	
	
	public void incrementPitch(int delta) {
		scaleIndex += delta;
		if (scaleIndex > scale.length-1) {
			octaveIndex += Math.floor(delta/scale.length)+1;
			scaleIndex = delta%scale.length;
			if(octaveIndex > 7) {
				octaveIndex = 7;				
			}
		} 
		
		if (scaleIndex < 0) {
			octaveIndex--;
			if(octaveIndex<0) {
				octaveIndex = 0;
				scaleIndex = 0;
			} else {
				scaleIndex = scale.length-1;
			}
		}
		
		pitch = scale[scaleIndex] + 12 * octaveIndex;
//		pitch += delta; 
//		if (pitch < -1) {
//			pitch = -1;
//		}
//		
//		if (pitch > 127) {
//			pitch = 127;
//		}
	}
	
	public void incrementVelocity(int delta) {
		velocity += delta; 
		if (velocity < -1) {
			velocity = -1;
		}
		
		if (velocity > 127) {
			velocity = 127;
		}
	}
	
	public void renderNormal(PApplet app) {
		app.fill(0);
		if (!isMute ) {
			super.renderNormal(app);
			app.textAlign(PConstants.CENTER);
			app.text(alphabet[scale[scaleIndex]] + Integer.toString(octaveIndex), position.x, position.y);
		} else {
			app.ellipse(position.x, position.y, radius, radius);
		}
		
	}
	
	public boolean getInstantSend() {
		return instantSend;
	}
	
	public void setInstantSend(boolean state) {
		instantSend = state;
	}
	
	public void setIsMute(boolean state) {
		isMute = state;
	}

	public void toggleMute() {
		isMute = !isMute;
		
	}
	
	
}
