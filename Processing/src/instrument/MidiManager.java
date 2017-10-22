package instrument;

import java.util.ArrayList;
import java.util.List;

import themidibus.MidiBus;

public class MidiManager {
	
	static class Signal {
		private int pitch = 64;
		private int velocity = 64;
		private float duration = 0.1f;
		private float time = 0;
		
		public Signal(int pitch, int velocity) {
			this.pitch = pitch;
			this.velocity = velocity;
		}
		
		public boolean equals(Object other) {
			Signal s = (Signal)other;
			return s.pitch == pitch && s.velocity == velocity;
		}
	}
	
	private boolean sustainOn = false;
	
	private List<Signal> signals = new ArrayList<Signal>();
	private List<Signal> signalsToAdd = new ArrayList<Signal>();
	
	private MidiBus bus;
	private int channel = 0;
	
	public MidiManager(MidiBus bus) {
		this.bus = bus;
	}
	
	public void addSignal(int pitch, int velocity) {
		Signal s = new Signal(pitch, velocity);
		int i = signalsToAdd.indexOf(s);
		if (i == -1) {
			signalsToAdd.add(s);
		}	
	}
	
	public void update(float dt) {
		// Adding signals slowly
		for (int i = signalsToAdd.size()-1; i >= 0; i--) {
			Signal s = signalsToAdd.get(i);
			
			int sigI = signals.indexOf(s); 
			if (sigI == -1) {
				signals.add(s);
			} else {
				Signal playing = signals.get(sigI); 
				playing.time = 0;
				sendNoteOff(s);
			}
			
			sendNoteOn(s);
			
			signalsToAdd.remove(i);
			if (i > 2) {
				break;
			}
		}
		
		if (!sustainOn) {
		// Without sustain
			for (int i = signals.size()-1; i >= 0; i--) {
				Signal s = signals.get(i);
				s.time += dt;

				if (s.time >= s.duration) {
					System.out.println("Removed!");
					signals.remove(i);
					sendNoteOff(s);
				}
			}
		}
	}
	
	public void setSustain(boolean state) {
		sustainOn = state;
	}
	
	private void sendNoteOn(Signal signal) {
		bus.sendNoteOn(channel, signal.pitch, signal.velocity);
	}
	
	private void sendNoteOff(Signal signal) {
		bus.sendNoteOff(channel, signal.pitch, signal.velocity);
	}
	
	
}
