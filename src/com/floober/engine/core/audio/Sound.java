package com.floober.engine.core.audio;

import static org.lwjgl.openal.AL10.*;

public class Sound {

	private final int bufferID;
	private float length;
	private final boolean isStereo;

	public Sound(int bufferID, boolean stereo) {
		this.bufferID = bufferID;
		this.isStereo = stereo;
		initLength();
	}

	private void initLength() {
		int bytes = alGetBufferi(bufferID, AL_SIZE);
		int bits = alGetBufferi(bufferID, AL_BITS);
		int channels = alGetBufferi(bufferID, AL_CHANNELS);
		int freq = alGetBufferi(bufferID, AL_FREQUENCY);
		int samples = bytes / (bits / 8);
		this.length = (float)samples / (float)freq / (float)channels;
	}

	public int getBufferID() {
		return bufferID;
	}
	public float getLength() { return length; }
	public boolean isStereo() {
		return isStereo;
	}
}